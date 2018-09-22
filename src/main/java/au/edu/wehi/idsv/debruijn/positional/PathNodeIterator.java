package au.edu.wehi.idsv.debruijn.positional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.PriorityQueue;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import au.edu.wehi.idsv.Defaults;
import au.edu.wehi.idsv.debruijn.KmerEncodingHelper;
import au.edu.wehi.idsv.util.IntervalUtil;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

/**
 * Transforms a start position sorted sequence of non-overlapping KmerAggregateNode to a
 * start position sorted sequence of KmerPathNode with graph edges 
 * @author Daniel Cameron
 *
 */
public class PathNodeIterator implements Iterator<KmerPathNode> {
	private final PeekingIterator<? extends KmerNode> underlying;
	private final int maxNodeLength;
	private final int k;
	/**
	 * Edge lookup. This lookup contains each KmerNode/KmerPathNode at the start/end kmers
	 * As a KmerPathNode is constructed, the KmerNode lookup entries are replaced
	 * a KmerPathNode at the current start and end kmers for that KmerPathNode
	 * 
	 * Intermediate KmerNode kmers are not required since
	 * - each KmerNode is processed when all potential edges have been added to the graph
	 * - KmerPathNodes are extends when there is only a single edge between successive KmerNodes
	 * - replacing successive KmerNodes with a KmerPathNode representing both edges is a
	 * local operation involving only those 2 nodes
	 * - therefore, only the start and end kmers for each KmerPathNode need be stored in this lookup
	 * 
	 */
	private final Long2ObjectOpenHashMap<List<KmerNode>> edgeLookup = new Long2ObjectOpenHashMap<List<KmerNode>>();
	/**
	 * Need a separate lookup for path node start kmers. Since we can start and end on the same kmer
	 * which causes duplicate next/prev paths for those nodes
	 */
	private final Long2ObjectOpenHashMap<List<KmerPathNode>> firstKmerEdgeLookup = new Long2ObjectOpenHashMap<List<KmerPathNode>>();
	/**
	 * Nodes that have not yet had all edges defined.
	 * 
	 * Edges are fully defined when the inputPosition is greater than the end position of the node
	 * - latest first kmer start position of node is equal to its endPosition
	 * - prev nodes must end before current not first kmer end position
	 * - next nodes must start no later than our end position + 1
	 */
	private final PriorityQueue<KmerNode> activeNodes = new PriorityQueue<KmerNode>(KmerNodeUtil.ByLastEnd);
	private final PriorityQueue<KmerPathNode> pathNodes = new PriorityQueue<KmerPathNode>(1024, KmerNodeUtil.ByFirstStart);
	private int inputPosition = Integer.MIN_VALUE;
	/**
	 * Maximum width of a single node. This is calculated from the input sequence
	 */
	private int maxNodeWidth = 0;
	private long consumed = 0;
	public PathNodeIterator(Iterator<? extends KmerNode> it, int maxPathLength, int k) {
		if (maxPathLength < 1) throw new IllegalArgumentException("Path length must be positive");
		this.underlying = Iterators.peekingIterator(it);
		this.maxNodeLength = maxPathLength;
		this.k = k;
	}
	/**
	 * Edge lookup
	 */
	private List<KmerNode> nextNodes(KmerNode node) {
		List<KmerNode> adj = new ArrayList<KmerNode>(4);
		for (long kmer : KmerEncodingHelper.nextStates(k, node.lastKmer())) {
			List<KmerNode> list = edgeLookup.get(kmer);
			if (list != null) {
				for (KmerNode n : list) {
					if (!(n instanceof KmerPathNode) && IntervalUtil.overlapsClosed(node.lastStart() + 1, node.lastEnd() + 1, n.firstStart(), n.firstEnd())) {
						assert(KmerEncodingHelper.isNext(k, node.lastKmer(), n.firstKmer()));
						adj.add(n);
					}
				}
			}
			List<KmerPathNode> pnList = firstKmerEdgeLookup.get(kmer);
			if (pnList != null) {
				for (KmerNode n : pnList) {
					if (IntervalUtil.overlapsClosed(node.lastStart() + 1, node.lastEnd() + 1, n.firstStart(), n.firstEnd())) {
						assert(KmerEncodingHelper.isNext(k, node.lastKmer(), n.firstKmer()));
						adj.add(n);
					}
				}
			}
		}
		return adj;
	}
	private List<KmerNode> prevNodes(KmerNode node) {
		List<KmerNode> adj = new ArrayList<KmerNode>(4);
		for (long kmer : KmerEncodingHelper.prevStates(k, node.firstKmer())) {
			List<KmerNode> list = edgeLookup.get(kmer);
			if (list != null) {
				for (KmerNode n : list) {
					if (IntervalUtil.overlapsClosed(n.lastStart() + 1, n.lastEnd() + 1, node.firstStart(), node.firstEnd())) {
						assert(KmerEncodingHelper.isNext(k, n.lastKmer(), node.firstKmer()));
						adj.add(n);
					}
				}
			}
		}
		return adj;
	}
	/**
	 * Replaces the given lookup node with the equivalent path node
	 * @param node
	 * @param pn
	 */
	private void lookupReplace(KmerNode node, KmerPathNode pn) {
		List<KmerNode> list = edgeLookup.get(node.lastKmer());
		assert(list != null);
		ListIterator<KmerNode> it = list.listIterator();
		while (it.hasNext()) {
			KmerNode n = it.next();
			if (n == node) {
				it.set(pn);
				return;
			}
		}
		throw new IllegalArgumentException(String.format("%s missing from edge lookup", node));
	}
	/**
	 * Adds the end kmer of the give node to the edge lookup
	 * @param node
	 */
	private void lookupAdd(KmerNode node) {
		List<KmerNode> list = edgeLookup.get(node.lastKmer());
		if (list == null) {
			list = new LinkedList<KmerNode>();
			edgeLookup.put(node.lastKmer(), list);
		}
		list.add(node);
	}
	private void lookupRemove(KmerNode node) {
		List<KmerNode> list = edgeLookup.get(node.lastKmer());
		assert(list != null);
		boolean found = list.remove(node);
		assert(found);
		if (list.isEmpty()) {
			edgeLookup.remove(node.lastKmer());
		}
	}
	private void firstKmerLookupAdd(KmerPathNode node) {
		List<KmerPathNode> list = firstKmerEdgeLookup.get(node.firstKmer());
		if (list == null) {
			list = new LinkedList<KmerPathNode>();
			firstKmerEdgeLookup.put(node.firstKmer(), list);
		}
		list.add(node);
	}
	private void firstKmerLookupRemove(KmerNode node) {
		List<KmerPathNode> list = firstKmerEdgeLookup.get(node.firstKmer());
		assert(list != null);
		boolean found = list.remove(node);
		assert(found);
		if (list.isEmpty()) {
			firstKmerEdgeLookup.remove(node.firstKmer());
		}
	}
	@Override
	public boolean hasNext() {
		boolean hasMore = !pathNodes.isEmpty() || !activeNodes.isEmpty() || underlying.hasNext();
		if (!hasMore) {
			assert(pathNodes.isEmpty());
			assert(activeNodes.isEmpty());
			assert(!underlying.hasNext());
			assert(edgeLookup.isEmpty());
			assert(firstKmerEdgeLookup.isEmpty());
		}
		return hasMore;
	}
	private void advance() {
		inputPosition = underlying.peek().lastStart();
		while (underlying.hasNext() && underlying.peek().lastStart() == inputPosition) {
			KmerNode node = underlying.next();
			consumed++;
			lookupAdd(node);
			activeNodes.add(node);
			maxNodeWidth = Math.max(maxNodeWidth, node.width());
		}
		if (Defaults.SANITY_CHECK_ASSEMBLY_GRAPH) {
			sanityCheck();
		}
	}
	private void merge() {
		while (!activeNodes.isEmpty() && activeNodes.peek().lastEnd() < inputPosition) {
			KmerNode node = activeNodes.poll();
			merge(node);
		}
		if (Defaults.SANITY_CHECK_ASSEMBLY_GRAPH) {
			sanityCheck();
		}
	}
	private void merge(KmerNode node) {
		// can we merge into an earlier KmerNode?
		List<KmerNode> prev = prevNodes(node);
		if (prev.size() == 1) {
			KmerNode toMerge = prev.get(0);
			if (toMerge.lastStart() == node.firstStart() - 1
					&& toMerge.lastEnd() == node.firstEnd() - 1
					&& toMerge.isReference() == node.isReference()
					&& toMerge.length() < maxNodeLength) {
				// we can merge
				assert(KmerEncodingHelper.isNext(k, toMerge.lastKmer(), node.firstKmer()));
				assert(toMerge instanceof KmerPathNode); // must have already processed our previous node
				KmerPathNode pn = (KmerPathNode)toMerge;
				List<KmerNode> pnNext = nextNodes(pn);
				if (pnNext.size() == 1) {
					assert(pnNext.get(0) == node);
					lookupRemove(pn);
					pn.append(node);
					lookupReplace(node, pn);
					return;
				}
			}
		}
		// couldn't merge into a previous path = new path
		KmerPathNode pn = new KmerPathNode(node);
		lookupReplace(node, pn);
		firstKmerLookupAdd(pn);
		pathNodes.add(pn);
	}
	@Override
	public KmerPathNode next() {
		while (underlying.hasNext() && (pathNodes.isEmpty() || !edgesCanBeFullyDefined(pathNodes.peek()))) {
			advance();
			merge();
		}
		if (!underlying.hasNext()) {
			// finish off
			inputPosition = Integer.MAX_VALUE;
			merge();
		}
		KmerPathNode pn = pathNodes.poll();
		assert(pn != null);
		assert(edgesCanBeFullyDefined(pn));
		// populate remaining edges
		for (KmerNode n : prevNodes(pn)) {
			assert(n instanceof KmerPathNode);
			KmerPathNode.addEdge((KmerPathNode)n, pn);
		}
		for (KmerNode n : nextNodes(pn)) {
			assert(n instanceof KmerPathNode);
			if (n != pn) {
				KmerPathNode.addEdge(pn, (KmerPathNode)n);
			}
		}
		// then remove node from lookup
		lookupRemove(pn);
		firstKmerLookupRemove(pn);
		assert(pn.length() <= maxNodeLength);
		if (Defaults.SANITY_CHECK_ASSEMBLY_GRAPH) {
			sanityCheck();
		}
		return pn;
	}
	private boolean edgesCanBeFullyDefined(KmerPathNode node) {
		// to still be unprocessed, the node has to end at or after inputPosition
		// therefore, earliest start position of a unprocessed node
		// is based on the maximum width of a node
		int earliestFirstKmerStartPositionOfActiveNode = inputPosition - maxNodeWidth;
		return node.lastEnd() < earliestFirstKmerStartPositionOfActiveNode;
	}
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
	public boolean sanityCheck() {
		for (KmerNode n : activeNodes) {
			assert(!pathNodes.contains(n));
			assert(edgeLookup.get(n.lastKmer()) != null);
			assert(edgeLookup.get(n.lastKmer()).contains(n));
			if (n instanceof KmerPathNode) {
				assert(firstKmerEdgeLookup.get(n.firstKmer()) != null);
				assert(firstKmerEdgeLookup.get(n.firstKmer()).contains(n));
				((KmerPathNode)n).sanityCheck();
			}
		}
		for (KmerPathNode n : pathNodes) {
			assert(!activeNodes.contains(n));
			assert(edgeLookup.get(n.lastKmer()) != null);
			assert(edgeLookup.get(n.lastKmer()).contains(n));
			assert(firstKmerEdgeLookup.get(n.firstKmer()) != null);
			assert(firstKmerEdgeLookup.get(n.firstKmer()).contains(n));
			n.sanityCheck();
		}
		assert(activeNodes.stream().distinct().count() == activeNodes.size());
		assert(pathNodes.stream().distinct().count() == pathNodes.size());
		return true;
	}
	public int tracking_processedSize() {
		return pathNodes.size();
	}
	public int tracking_activeSize() {
		return activeNodes.size();
	}
	public int tracking_inputPosition() {
		return inputPosition;
	}
	public long tracking_underlyingConsumed() {
		return consumed;
	}
	public int tracking_edgeLookupSize() {
		return edgeLookup.size();
	}
	public int tracking_pathNodeEdgeLookupSize() {
		return firstKmerEdgeLookup.size();
	}
	public int tracking_edgeLookupMaxKmerNodeCount() {
		return edgeLookup.values().stream().mapToInt(x -> x.size()).max().orElse(0);
	}
	public int tracking_pathNodeEdgeLookupMaxKmerNodeCount() {
		return firstKmerEdgeLookup.values().stream().mapToInt(x -> x.size()).max().orElse(0);
	}
}
