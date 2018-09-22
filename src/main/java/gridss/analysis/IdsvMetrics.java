package gridss.analysis;

import htsjdk.samtools.metrics.MetricBase;

public class IdsvMetrics extends MetricBase {
	/**
	 * Length of longest read
	 */
	public int MAX_READ_LENGTH = 0;
	/**
	 * Maximum number of reference bases this read spans.
	 * 
	 * This is usually longer than MAX_READ_LENGTH due to the presence of deletions with a read CIGAR
	 */
	public int MAX_READ_MAPPED_LENGTH = 0;
	/**
	 * Inferred size of largest concordantly mapped fragment including all read bases of both pairs
	 * Note: This differs from the htsjdk definition of fragment size 
	 */
	public Integer MAX_PROPER_PAIR_FRAGMENT_LENGTH = null;
	/**
	 * Inferred size of largest concordantly mapped fragment including all read bases of both pairs
	 * Note: This differs from the htsjdk definition of fragment size 
	 */
	public Integer MIN_PROPER_PAIR_FRAGMENT_LENGTH = null;
	/**
	 * Number of reads
	 */
	public long READS = 0;
	/**
	 * Number of mapped reads
	 */
	public long MAPPED_READS = 0;
	/**
	 * Number of paired reads
	 */
	public long READ_PAIRS = 0;
	/**
	 * Number of paired reads
	 */
	public long READ_PAIRS_BOTH_MAPPED = 0;
	/**
	 * Number of read pairs where only one read is mapped
	 */
	public long READ_PAIRS_ONE_MAPPED = 0;
	/**
	 * Number of read pairs where no read is mapped
	 */
	public long READ_PAIRS_ZERO_MAPPED = 0;
	/**
	 * Number of secondary alignments that are not split read alignments.
	 */
	public long SECONDARY_NOT_SPLIT = 0;
}
