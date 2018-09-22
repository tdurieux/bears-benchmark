package au.edu.wehi.idsv.debruijn.positional;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Set;

import au.edu.wehi.idsv.util.IntervalUtil;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/**
 * Tracks evidence provided to a given graph by wrapping a source iterator
 * and tracking evidence emitted by the iterator 
 * 
 * @author Daniel Cameron
 *
 */
public class EvidenceTracker {
	//public static EvidenceTracker TEMP_HACK_CURRENT_TRACKER = null;
	private final Long2ObjectOpenHashMap<LinkedList<KmerSupportNode>> lookup = new Long2ObjectOpenHashMap<LinkedList<KmerSupportNode>>();
	private final ObjectOpenHashSet<String> id = new ObjectOpenHashSet<String>();
	private long evidenceTotal = 0;
	/**
	 * Tracks evidence emitted from the given iterator
	 * @param it iterator to track
	 */
	public EvidenceTracker() {
	}
	/**
	 * Tracks the given evidence
	 * @param evidence
	 */
	public KmerSupportNode track(KmerSupportNode support) {
		long kmer = support.lastKmer();
		LinkedList<KmerSupportNode> list = lookup.get(kmer);
		if (list == null) {
			list = new LinkedList<KmerSupportNode>();
			lookup.put(kmer, list);
		}
		list.add(support);
		if (id.add(support.evidence().evidence().getEvidenceID())) {
			evidenceTotal++;
		}
		return support;
	}
	/**
	 * Stops tracking all nodes associated with the given evidence 
	 * @param evidence
	 */
	public void remove(KmerEvidence evidence) {
		for (int i = 0; i < evidence.length(); i++) {
			long kmer = evidence.kmer(i);
			remove(kmer, evidence);
		}
		id.remove(evidence.evidence().getEvidenceID());
	}
	/**
	 * Stops tracking all nodes associated with the given evidence 
	 * @param evidence
	 */
	private void remove(long kmer, KmerEvidence evidence) {
		LinkedList<KmerSupportNode> list = lookup.get(kmer);
		if (list != null) {
			ListIterator<KmerSupportNode> it = list.listIterator();
			while (it.hasNext()) {
				KmerSupportNode n = it.next();
				if (n.evidence() == evidence) { 
					it.remove();
				}
			}
			if (list.size() == 0) {
				lookup.remove(kmer);
			}
		}
	}
	/**
	 * Identifies evidence supporting the given path
	 * @param contig path
	 * @return all evidence supporting the given path
	 */
	public Set<KmerEvidence> support(Collection<KmerPathSubnode> contig) {
		return traverse(contig, false);
	}
	/**
	 * Stops tracking all evidence overlapping the given contig
	 * @param contig contig to stop tracking
	 * @return removed evidence 
	 */
	public Set<KmerEvidence> untrack(Collection<KmerPathSubnode> contig) {
		return traverse(contig, true);
	}
	public Set<KmerEvidence> traverse(Collection<KmerPathSubnode> contig, boolean remove) {
		Set<KmerEvidence> evidence = Collections.newSetFromMap(new IdentityHashMap<KmerEvidence, Boolean>());
		for (KmerPathSubnode sn : contig) {
			int start = sn.firstStart();
			int end = sn.firstEnd();
			for (int i = 0; i < sn.length(); i++) {
				toCollection(evidence, sn.kmer(i), start + i, end + i, remove);
			}
			LongArrayList collapsed = sn.node().collapsedKmers();
			IntArrayList collapsedOffset = sn.node().collapsedKmerOffsets();
			for (int i = 0; i < collapsed.size(); i++) {
				int offset = collapsedOffset.getInt(i);
				toCollection(evidence, collapsed.getLong(i), start + offset, end + offset, remove);
			}
		}
		if (remove) {
			for (KmerEvidence e : evidence) {
				// remove any leftover evidence kmers not on the called path   
				remove(e);
			}
		}
		return evidence;
	}
	/**
	 * Stops tracking all evidence overlapping the given kmer interval and adds to the given collection
	 * 
	 * @param collection
	 * @param kmer
	 * @param start
	 * @param end
	 */
	private void toCollection(Collection<KmerEvidence> collection, long kmer, int start, int end, boolean remove) {
		LinkedList<KmerSupportNode> list = lookup.get(kmer);
		if (list != null) {
			ListIterator<KmerSupportNode> it = list.listIterator();
			while (it.hasNext()) {
				KmerSupportNode n = it.next();
				if (IntervalUtil.overlapsClosed(start, end, n.lastStart(), n.lastEnd())) {
					if (remove) {
						it.remove();
					}
					collection.add(n.evidence());
				}
			}
		}
	}
	public boolean matchesExpected(KmerPathSubnode pn) {
		for (int i = 0; i < pn.length(); i++) {
			LongArrayList kmers = new LongArrayList();
			kmers.add(pn.kmer(i));
			for (int j = 0; j < pn.node().collapsedKmerOffsets().size(); j++) {
				if (pn.node().collapsedKmerOffsets().getInt(j) == i) {
					kmers.add(pn.node().collapsedKmers().getLong(j));
				}
			}
			if (!matchesExpected(pn.weight(i) * pn.width(), kmers, pn.firstStart() + i, pn.firstEnd() + i)) {
				return false;
			}
		}
		return true;
	}
	public boolean matchesExpected(int expectedWidthWeight, LongArrayList kmers, int start, int end) {
		int evidenceWeight = 0;
		for (long kmer : kmers) {
			LinkedList<KmerSupportNode> list = lookup.get(kmer);
			if (list != null) {
				for (KmerSupportNode n : list) {
					evidenceWeight += n.weight() * IntervalUtil.overlapsWidthClosed(start, end, n.lastStart(), n.lastEnd());
				}
			}
		}
		assert(evidenceWeight == expectedWidthWeight);
		return evidenceWeight == expectedWidthWeight;
	}
	public boolean isTracked(String evidenceId) {
		return id.contains(evidenceId);
	}
	public class PathNodeAssertionInterceptor implements Iterator<KmerPathNode> {
		private final Iterator<KmerPathNode> underlying;
		private final String id;
		public PathNodeAssertionInterceptor(Iterator<KmerPathNode> it, String id) {
			this.underlying = it;
			this.id = id;
		}
		@Override
		public boolean hasNext() {
			return underlying.hasNext();
		}
		@Override
		public KmerPathNode next() {
			KmerPathNode node = underlying.next();
			assert(matchesExpected(new KmerPathSubnode(node)));
			return node;
		}
		@Override
		public String toString() {
			return id;
		}
	}
	public class AggregateNodeAssertionInterceptor implements Iterator<KmerNode> {
		private final Iterator<KmerNode> underlying;
		public AggregateNodeAssertionInterceptor(Iterator<KmerNode> it) {
			this.underlying = it;
		}
		@Override
		public boolean hasNext() {
			return underlying.hasNext();
		}
		@Override
		public KmerNode next() {
			KmerNode node = underlying.next();
			assert(matchesExpected(node.width() * node.weight(), LongArrayList.wrap(new long[] { node.firstKmer() }), node.firstStart(), node.firstEnd()));
			return node;
		}
	}
	public long tracking_evidenceTotal() {
		return evidenceTotal;
	}
	public long tracking_evidenceActive() {
		return id.size();
	}
	public int tracking_kmerCount() {
		return lookup.size();
	}
	public int tracking_supportNodeCount() {
		return lookup.values().stream().mapToInt(x -> x.size()).sum();
	}
	public int tracking_maxKmerSupportNodesCount() {
		return lookup.values().stream().mapToInt(x -> x.size()).max().orElse(0);
	}
}
