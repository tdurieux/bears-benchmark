package au.edu.wehi.idsv.debruijn.positional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

import au.edu.wehi.idsv.TestHelper;


public class EvidenceTrackerTest extends TestHelper {
	@Test
	public void shouldTrackWhenIterated() {
		int k = 4;
		List<KmerSupportNode> list = new ArrayList<KmerSupportNode>();
		list.add(KmerEvidence.create(k, SCE(FWD, Read(0, 1, "4M1S"))).node(0));
		EvidenceTracker tracker = new EvidenceTracker();
		Set<KmerEvidence> result = tracker.untrack(ImmutableList.of(new KmerPathSubnode(KPN(k, "AAAA", 1, 1, true))));
		assertEquals(0, result.size());
		tracker.track(list.get(0));
		result = tracker.untrack(ImmutableList.of(new KmerPathSubnode(KPN(k, "AAAA", 1, 1, true))));
		assertEquals(1, result.size());
		assertEquals(list.get(0).evidence(), result.iterator().next());
	}
	@Test
	public void shouldUntrackAllEvidenceNodes() {
		int k = 4;
		KmerEvidence e = KmerEvidence.create(k, SCE(FWD, Read(0, 1, "4M1S")));
		List<KmerSupportNode> list = new ArrayList<KmerSupportNode>();
		list.add(e.node(0));
		list.add(e.node(1));
		EvidenceTracker tracker = new EvidenceTracker();
		tracker.track(list.get(0));
		tracker.track(list.get(1));
		Set<KmerEvidence> result = tracker.untrack(ImmutableList.of(new KmerPathSubnode(KPN(k, "AAAA", 1, 1, true))));
		assertEquals(1, result.size());
		result = tracker.untrack(ImmutableList.of(new KmerPathSubnode(KPN(k, "AAAA", 1, 1, true))));
		assertEquals(0, result.size());
		result = tracker.untrack(ImmutableList.of(new KmerPathSubnode(KPN(k, "AAAA", 2, 2, true))));
		assertEquals(0, result.size());
	}
	@Test
	public void shouldTrackEvidenceID() {
		int k = 4;
		KmerEvidence e = KmerEvidence.create(k, SCE(FWD, Read(0, 1, "4M1S")));
		List<KmerSupportNode> list = new ArrayList<KmerSupportNode>();
		list.add(e.node(0));
		EvidenceTracker tracker = new EvidenceTracker();
		assertFalse(tracker.isTracked(e.evidence().getEvidenceID()));
		tracker.track(list.get(0));
		assertTrue(tracker.isTracked(e.evidence().getEvidenceID()));
		tracker.remove(e);
		assertFalse(tracker.isTracked(e.evidence().getEvidenceID()));
	}
}
