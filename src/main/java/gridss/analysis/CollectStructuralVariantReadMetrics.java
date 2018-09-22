package gridss.analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.broadinstitute.barclay.argparser.Argument;
import org.broadinstitute.barclay.argparser.CommandLineProgramProperties;

import au.edu.wehi.idsv.picard.ReferenceLookup;
import au.edu.wehi.idsv.sam.SAMRecordUtil;
import gridss.ExtractSVReads;
import gridss.cmdline.ProcessStructuralVariantReadsCommandLineProgram;
import gridss.filter.ClippedReadFilter;
import gridss.filter.IndelReadFilter;
import gridss.filter.OneEndAnchoredReadFilter;
import gridss.filter.ReadPairConcordanceFilter;
import gridss.filter.SplitReadFilter;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.filter.AlignedFilter;
import htsjdk.samtools.metrics.MetricsFile;

@CommandLineProgramProperties(
		summary = "Collects metrics regarding the structural variant reads present in the input.",
		oneLineSummary = "Collects metrics regarding the structural variant reads present in the input.",
        programGroup = gridss.cmdline.programgroups.Metrics.class
)
public class CollectStructuralVariantReadMetrics extends ProcessStructuralVariantReadsCommandLineProgram {
	public static final String METRICS_SUFFIX = ".sv_metrics";
	
	@Argument(doc = "Include secondary alignments in read counts", optional=true)
    public boolean COUNT_SECONDARY = false;
	
	@Argument(doc = "Include supplementary alignments in read counts", optional=true)
    public boolean COUNT_SUPPLEMENTARY = false;
	
	//private static final Log log = Log.getInstance(CollectStructuralVariantReadMetrics.class);
	public static void main(String[] argv) {
        System.exit(new CollectStructuralVariantReadMetrics().instanceMain(argv));
    }
	private IndelReadFilter indelFilter;
	private ClippedReadFilter softClipFilter;
	private SplitReadFilter splitReadFilter;
	private AlignedFilter unmappedFilter;
	private OneEndAnchoredReadFilter oeaFilter;
	private ReadPairConcordanceFilter dpFilter;
	private StructuralVariantReadMetrics metrics;
	@Override
	public void setup(SAMFileHeader header, File samFile) {
		indelFilter = new IndelReadFilter(MIN_INDEL_SIZE);
		softClipFilter = new ClippedReadFilter(MIN_CLIP_LENGTH); 
		splitReadFilter = new SplitReadFilter();
		unmappedFilter = new AlignedFilter(false);
		oeaFilter = new OneEndAnchoredReadFilter();
		dpFilter = getReadPairConcordanceCalculator() != null ? new ReadPairConcordanceFilter(getReadPairConcordanceCalculator(), false, true) : null;
		metrics = new StructuralVariantReadMetrics();
	}
	@Override
	public void acceptFragment(List<SAMRecord> records, ReferenceLookup lookup) {
		if (!INCLUDE_DUPLICATES) {
			records = new ArrayList<>(records);
			for (int i = records.size() - 1; i >= 0; i--) {
				if (records.get(i).getDuplicateReadFlag()) {
					records.remove(i);
				}
			}
		}
		boolean hasConsistentReadPair = ExtractSVReads.hasReadPairingConsistentWithReference(getReadPairConcordanceCalculator(), records);
		boolean[] hasConsistentReadAlignment = ExtractSVReads.hasReadAlignmentConsistentWithReference(records);
		boolean hasOeaAnchor = false;
		boolean hasDp = false;
		int maxSegmentIndex = 0;
		boolean[] hasIndel = new boolean[hasConsistentReadAlignment.length];
		boolean[] hasSoftClip = new boolean[hasConsistentReadAlignment.length];
		boolean[] hasSplitRead = new boolean[hasConsistentReadAlignment.length];
		boolean[] hasUnmapped = new boolean[hasConsistentReadAlignment.length];
		boolean[] hasSV = new boolean[hasConsistentReadAlignment.length];
		for (SAMRecord r : records) {
			if (r.isSecondaryAlignment() && !COUNT_SECONDARY) { 
				continue;
			}
			if (r.getSupplementaryAlignmentFlag() && !COUNT_SUPPLEMENTARY) { 
				continue;
			}
			// Read pairing
			if (!hasConsistentReadPair) {
				if (SINGLE_MAPPED_PAIRED && !oeaFilter.filterOut(r) && !r.getReadUnmappedFlag()) {
					metrics.UNMAPPED_MATE_READ_ALIGNMENTS++;
					hasOeaAnchor = true;
				}
				if (dpFilter != null && DISCORDANT_READ_PAIRS && !dpFilter.filterOut(r)) {
					metrics.DISCORDANT_READ_PAIR_ALIGNMENTS++;
					hasDp = true;
				}
			}
			int index = SAMRecordUtil.getSegmentIndex(r);
			maxSegmentIndex = Math.max(index, maxSegmentIndex);
			boolean isFullyMapped = hasConsistentReadAlignment[index];
			if (!isFullyMapped) {
				boolean isSVAlignment = false;
				if (INDELS && !indelFilter.filterOut(r)) {
					metrics.INDEL_READ_ALIGNMENTS++;
					hasIndel[index] = true;
					hasSV[index] = true;
					isSVAlignment = true;
				}
				if (CLIPPED && !softClipFilter.filterOut(r)) {
					metrics.SOFT_CLIPPED_READ_ALIGNMENTS++;
					hasSoftClip[index] = true;
					hasSV[index] = true;
					isSVAlignment = true;
				}
				if (SPLIT && !splitReadFilter.filterOut(r)) {
					metrics.SPLIT_READ_ALIGNMENTS++;
					hasSplitRead[index] = true;
					hasSV[index] = true;
					isSVAlignment = true;
				}
				if (UNMAPPED_READS && !unmappedFilter.filterOut(r)) {
					hasUnmapped[index] = true;
					hasSV[index] = true;
					isSVAlignment = true;
				}
				if (isSVAlignment) {
					metrics.STRUCTURAL_VARIANT_READ_ALIGNMENTS++;
				}
			}
		}
		if (hasOeaAnchor) {
			metrics.UNMAPPED_MATE_READS++;
		}
		if (hasDp) {
			metrics.DISCORDANT_READ_PAIRS++;
		}
		if (hasOeaAnchor || hasDp) {
			metrics.STRUCTURAL_VARIANT_READ_PAIRS++;
		}
		metrics.INDEL_READS += countTrues(hasIndel, maxSegmentIndex);
		metrics.SOFT_CLIPPED_READS += countTrues(hasSoftClip, maxSegmentIndex);
		metrics.SPLIT_READS += countTrues(hasSplitRead, maxSegmentIndex);
		metrics.UNMAPPED_READS += countTrues(hasUnmapped, maxSegmentIndex);
		metrics.STRUCTURAL_VARIANT_READS += countTrues(hasSV, maxSegmentIndex);
	}
	private int countTrues(boolean[] arr, int maxIndex) {
		int count = 0;
		for (int i = 0; i < arr.length && i <= maxIndex; i++) {
			if (arr[i]) {
				count++;
			}
		}
		return count;
	}
	@Override
	public void finish() {
		final MetricsFile<StructuralVariantReadMetrics, Integer> metricsFile = getMetricsFile();
		metricsFile.addMetric(metrics);
        metricsFile.write(OUTPUT);
	}
	@Override
	public boolean referenceRequired() { return false; }
}
