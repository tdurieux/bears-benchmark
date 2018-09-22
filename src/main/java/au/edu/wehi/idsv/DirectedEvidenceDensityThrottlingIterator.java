package au.edu.wehi.idsv;

import java.util.Iterator;

import au.edu.wehi.idsv.bed.IntervalBed;
import au.edu.wehi.idsv.util.DensityThrottlingIterator;
import htsjdk.samtools.SAMSequenceDictionary;
import htsjdk.samtools.util.Log;

/**
 * Reduces
 * @author Daniel Cameron
 *
 */
public class DirectedEvidenceDensityThrottlingIterator extends DensityThrottlingIterator<DirectedEvidence> {
	private static final Log log = Log.getInstance(DirectedEvidenceDensityThrottlingIterator.class);
	private final LinearGenomicCoordinate lgc;
	private final SAMSequenceDictionary dictionary;
	private final IntervalBed throttled;
	private DirectedEvidence tresholdStart = null;
	public DirectedEvidenceDensityThrottlingIterator(IntervalBed throttled, SAMSequenceDictionary dictionary, LinearGenomicCoordinate lgc, Iterator<DirectedEvidence> it, int windowSize, double acceptDensity, double maxDensity) {
		super(it, windowSize, acceptDensity, maxDensity);
		this.lgc = lgc;
		this.dictionary = dictionary;
		this.throttled = throttled;
	}
	@Override
	protected long getPosition(DirectedEvidence record) {
		return lgc.getLinearCoordinate(record.getBreakendSummary().referenceIndex, record.getBreakendSummary().start);
	}
	@Override
	public DirectedEvidence next() {
		DirectedEvidence evidence = super.next();
		if (!isBelowUnconditionalAcceptanceThreshold() && tresholdStart == null) {
			tresholdStart = evidence;
		} else if (isBelowUnconditionalAcceptanceThreshold() && tresholdStart != null) {			
			int startReferenceIndex = tresholdStart.getBreakendSummary().referenceIndex;
			int startPos = tresholdStart.getBreakendSummary().start;
			int endReferenceIndex = evidence.getBreakendSummary().referenceIndex;
			int endPos = evidence.getBreakendSummary().start;
			if (startReferenceIndex == endReferenceIndex) {
				throttled.addInterval(startReferenceIndex, startPos, endPos);
			} else {
				throttled.addInterval(startReferenceIndex, startPos, dictionary.getSequence(startReferenceIndex).getSequenceLength());
				throttled.addInterval(endReferenceIndex, 1, endPos);
			}
			log.debug(String.format("Throttled assembly evidence in interval %s:%d-%d", dictionary.getSequence(startReferenceIndex).getSequenceName(), startPos, endPos));
			tresholdStart = null;
		}
		return evidence;
	}
}
