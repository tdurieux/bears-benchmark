package au.edu.wehi.idsv;

import htsjdk.samtools.SAMRecord;

public class UnmappedMateReadPair extends NonReferenceReadPair {
	protected UnmappedMateReadPair(SAMRecord local, SAMRecord remote, SAMEvidenceSource source) {
		super(local, remote, source);
	}
	@Override
	public String toString() {
		return String.format("UM %s MQ=%d RN=%s EID=%s", getBreakendSummary(), getLocalMapq(), getLocalledMappedRead().getReadName(), getEvidenceID());
	}
	@Override
	public float getBreakendQual() {
		return (float)getEvidenceSource().getContext().getConfig().getScoring().getModel().scoreUnmappedMate(getEvidenceSource().getMetrics(), getLocalMapq());
	}
}
