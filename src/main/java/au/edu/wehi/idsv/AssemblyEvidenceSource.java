package au.edu.wehi.idsv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.util.concurrent.MoreExecutors;

import au.edu.wehi.idsv.bed.IntervalBed;
import au.edu.wehi.idsv.configuration.AssemblyConfiguration;
import au.edu.wehi.idsv.debruijn.positional.PositionalAssembler;
import au.edu.wehi.idsv.sam.CigarUtil;
import au.edu.wehi.idsv.sam.SAMFileUtil;
import au.edu.wehi.idsv.sam.SAMRecordUtil;
import au.edu.wehi.idsv.util.FileHelper;
import au.edu.wehi.idsv.visualisation.AssemblyTelemetry;
import gridss.SoftClipsToSplitReads;
import gridss.cmdline.CommandLineProgramHelper;
import htsjdk.samtools.QueryInterval;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.SAMFileHeader.SortOrder;
import htsjdk.samtools.SAMFileWriter;
import htsjdk.samtools.SAMFileWriterFactory;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.util.CloseableIterator;
import htsjdk.samtools.util.Log;
import htsjdk.samtools.util.ProgressLogger;
import picard.sam.GatherBamFiles;

/**
 * Structural variant supporting contigs generated from assembly
 * 
 *  TODO: define separate functions for the attributes of the assemblies, and the source reads.
 *  It is currently unclear whether getMaxReadLength() refers to the assembly or source
 *  reads (it is the latter).
 *  
 * @author Daniel Cameron
 *
 */
public class AssemblyEvidenceSource extends SAMEvidenceSource {
	private static final Log log = Log.getInstance(AssemblyEvidenceSource.class);
	private final List<SAMEvidenceSource> source;
	private final IntervalBed throttled;
	private int cachedMaxSourceFragSize = -1;
	private int cachedMinConcordantFragmentSize = -1;
	private int cachedMaxReadLength = -1;
	private int cachedMaxReadMappedLength = -1;
	private AssemblyTelemetry telemetry;
	/**
	 * Generates assembly evidence based on the given evidence
	 * @param evidence evidence for creating assembly
	 */
	public AssemblyEvidenceSource(ProcessingContext processContext, List<SAMEvidenceSource> evidence, File assemblyFile) {
		super(processContext, assemblyFile, null, -1);
		this.source = evidence;
		this.throttled = new IntervalBed(getContext().getDictionary(), getContext().getLinear());
	}
	/**
	 * Perform breakend assembly 
	 * @param threadpool 
	 * @throws IOException 
	 */
	public void assembleBreakends(ExecutorService threadpool) throws IOException {
		source.stream().forEach(ses -> ses.assertPreprocessingComplete());
		if (threadpool == null) {
			threadpool = MoreExecutors.newDirectExecutorService();
		}
		invalidateSummaryCache();
		if (getContext().getConfig().getVisualisation().assemblyTelemetry) {
			telemetry = new AssemblyTelemetry(getContext().getFileSystemContext().getAssemblyTelemetry(getFile()), getContext().getDictionary());
		}
		List<QueryInterval[]> chunks = getContext().getReference().getIntervals(getContext().getConfig().chunkSize, getContext().getConfig().chunkSequenceChangePenalty);
		List<File> assembledChunk = new ArrayList<>();
		List<Future<Void>> tasks = new ArrayList<>();
		for (int i = 0; i < chunks.size(); i++) {
			QueryInterval[] chunk = chunks.get(i);
			File f = getContext().getFileSystemContext().getAssemblyChunkBam(getFile(), i);
			int chunkNumber = i;
			assembledChunk.add(f);
			if (!f.exists()) {
				tasks.add(threadpool.submit(() -> { assembleChunk(f, chunkNumber, chunk); return null; }));
			}
			
		}
		runTasks(tasks);
		if (telemetry != null) {
			telemetry.close();
			telemetry = null;
		}
		log.info("Breakend assembly complete.");
		List<File> deduplicatedChunks = assembledChunk;
		long secondaryNotSplit = source.stream().mapToLong(ses -> ses.getMetrics().getIdsvMetrics().SECONDARY_NOT_SPLIT).sum();
		if (secondaryNotSplit > 0) {
			log.warn(String.format("Found %d secondary alignments that were not split read alignments. GRIDSS no longer supports multi-mapping alignment. These reads will be ignored.", secondaryNotSplit));
		}
		log.info("Merging assembly files");
		// Merge chunk files
		File out = getFile();
		File tmpout = gridss.Defaults.OUTPUT_TO_TEMP_FILE ? FileSystemContext.getWorkingFileFor(getFile()) : out;
		GatherBamFiles gather = new picard.sam.GatherBamFiles();
		List<String> args = new ArrayList<>();
		for (File f : deduplicatedChunks) {
			args.add("INPUT=" + f.getPath());
		}
		args.add("OUTPUT=" + tmpout.getPath());
		if (getContext().getCommandLineProgram() != null) {
			args.addAll(CommandLineProgramHelper.getCommonArgs(getContext().getCommandLineProgram()));
		}
		int returnCode = gather.instanceMain(args.toArray(new String[] {}));
		if (returnCode != 0) {
			String msg = String.format("Error executing GatherBamFiles. GatherBamFiles returned status code %d", returnCode);
			log.error(msg);
			throw new RuntimeException(msg);
		}
		// Sorting is not required since each chunk was already sorted, and each chunk
		// contains sequential genomic coordinates. We also don't need to index as we only need assembly.sv.bam indexed
		// SAMFileUtil.sort(getContext().getFileSystemContext(), tmpout, getFile(), SortOrder.coordinate);
		if (tmpout != out) {
			FileHelper.move(tmpout, out, true);
		}
		invalidateSummaryCache();
		if (gridss.Defaults.DELETE_TEMPORARY_FILES) {
			if (tmpout != out) {
				FileHelper.delete(tmpout, true);
			}
			for (File f : assembledChunk) {
				FileHelper.delete(f, true);
			}
			for (File f : deduplicatedChunks) {
				FileHelper.delete(f, true);
			}
		}
		File throttledFilename = new File(getFile().getPath() + ".throttled.bed");
		try {
			if (throttled.size() > 0) {
				throttled.write(throttledFilename, "Regions of high coverage where only a subset of supporting reads were considered for assembly");
			}
		} catch (IOException e) {
			log.warn(e, "Unable to write " + throttledFilename.getPath());
		}
	}
	private void runTasks(List<Future<Void>> tasks) {
		// Assemble as much as we can before dying
		Exception firstException = null;
		for (Future<Void> f : tasks) {
			try {
				f.get();
			} catch (Exception e) {
				if (firstException == null) {
					firstException = e;
				}
			}
		}
		log.info("Breakend assembly complete.");
		if (firstException != null) {
			log.error(firstException, "Fatal error during assembly ");
			throw new RuntimeException(firstException);
		}
	}
	private void assembleChunk(File output, int chunkNumber, QueryInterval[] qi) throws IOException {
		AssemblyIdGenerator assemblyNameGenerator = new SequentialIdGenerator(String.format("asm%d-", chunkNumber));
		String chuckName = String.format("chunk %d (%s:%d-%s:%d)", chunkNumber,
			getContext().getDictionary().getSequence(qi[0].referenceIndex).getSequenceName(), qi[0].start,
			getContext().getDictionary().getSequence(qi[qi.length-1].referenceIndex).getSequenceName(), qi[qi.length-1].end);
		log.info(String.format("Starting assembly on %s", chuckName));
		Stopwatch timer = Stopwatch.createStarted();
		SAMFileHeader header = getContext().getBasicSamHeader();
		// TODO: add assembly @PG header
		File filteredout = FileSystemContext.getWorkingFileFor(output, "filtered.");
		File tmpout = FileSystemContext.getWorkingFileFor(output, "gridss.tmp.");
		try (SAMFileWriter writer = new SAMFileWriterFactory().makeSAMOrBAMWriter(header, false, tmpout)) {
			if (getContext().getAssemblyParameters().writeFiltered) {
				try (SAMFileWriter filteredWriter = new SAMFileWriterFactory().makeSAMOrBAMWriter(header, false, filteredout)) {
					for (BreakendDirection direction : BreakendDirection.values()) {
						assembleChunk(writer, filteredWriter, chunkNumber, qi, direction, assemblyNameGenerator);
					}
				}
			} else {
				for (BreakendDirection direction : BreakendDirection.values()) {
					assembleChunk(writer, null, chunkNumber, qi, direction, assemblyNameGenerator);
				}
			}
		} catch (Exception e) {
			log.error(e, "Error assembling ", chuckName);
			if (getContext().getConfig().terminateOnFirstError) {
				System.exit(1);
			}
			throw e;
		} finally {
			timer.stop();
			log.info(String.format("Completed assembly on %s in %ds (%s)", chuckName, timer.elapsed(TimeUnit.SECONDS), timer.toString()));
		}
		SAMFileUtil.sort(getContext().getFileSystemContext(), tmpout, output, SortOrder.coordinate);
		if (gridss.Defaults.DELETE_TEMPORARY_FILES) {
			tmpout.delete();
			filteredout.delete();
		}
		if (gridss.Defaults.DEFENSIVE_GC) {
			log.debug("Requesting defensive GC to ensure OS file handles are closed");
			System.gc();
			System.runFinalization();
		}
	}
	private QueryInterval[] getExpanded(QueryInterval[] intervals) {
		QueryInterval[] expanded = QueryIntervalUtil.padIntervals(
				getContext().getDictionary(),
				intervals,
				// expand bounds to keep any contig that could overlap our intervals
				(int)(2 * getMaxConcordantFragmentSize() * getContext().getConfig().getAssembly().maxExpectedBreakendLengthMultiple) + 1);
		return expanded;
	}
	private void assembleChunk(SAMFileWriter writer, SAMFileWriter filteredWriter, int chunkNumber, QueryInterval[] intervals, BreakendDirection direction, AssemblyIdGenerator assemblyNameGenerator) {
		QueryInterval[] expanded = getExpanded(intervals);
		try (CloseableIterator<DirectedEvidence> input = mergedIterator(source, expanded)) {
			Iterator<DirectedEvidence> throttledIt = throttled(input);
			PositionalAssembler assembler = new PositionalAssembler(getContext(), AssemblyEvidenceSource.this, assemblyNameGenerator, throttledIt, direction);
			if (telemetry != null) {
				assembler.setTelemetry(telemetry.getTelemetry(chunkNumber, direction));
			}
			while (assembler.hasNext()) {
				SAMRecord asm = assembler.next();
				asm = transformAssembly(asm); // transform before chunk bounds checking as the position may have moved
				if (QueryIntervalUtil.overlaps(intervals, asm.getReferenceIndex(), asm.getAlignmentStart())) {
					// only output assemblies that start within our chunk
					if (shouldFilterAssembly(asm)) {
						if (filteredWriter != null) {
							filteredWriter.addAlignment(asm);
						}
					} else {
						writer.addAlignment(asm);
					}
				}
			}
		}
	}
	@Override
	public synchronized void ensureExtracted() throws IOException {
		ensureMetrics();
		File svFile = getContext().getFileSystemContext().getSVBam(getFile());
		File withsplitreadsFile = FileSystemContext.getWorkingFileFor(svFile, "gridss.tmp.withsplitreads.");
		ensureMetrics();
		if (!svFile.exists()) {
			log.info("Identifying split reads for " + getFile().getAbsolutePath());
			List<String> args = Lists.newArrayList(
					"WORKER_THREADS=" + getProcessContext().getWorkerThreadCount(),
					"INPUT=" + getFile().getPath(),
					"OUTPUT=" + svFile.getPath(),
					"REALIGN_ENTIRE_READ=" + Boolean.toString(getContext().getConfig().getAssembly().realignContigs));
			execute(new SoftClipsToSplitReads(), args);
		}
		SAMFileUtil.sort(getContext().getFileSystemContext(), withsplitreadsFile, svFile, SortOrder.coordinate);
	}
	@Override
	public boolean shouldFilter(SAMRecord r) {
		if (r.hasAttribute("OA") && !SAMRecordUtil.overlapsOriginalAlignment(r)) {
			return true;
		}
		return super.shouldFilter(r);
	}
	public boolean shouldFilterAssembly(SAMRecord asm) {
		AssemblyConfiguration ap = getContext().getAssemblyParameters();
		AssemblyAttributes attr = new AssemblyAttributes(asm);
		// reference assembly
		List<SingleReadEvidence> breakends = SingleReadEvidence.createEvidence(this, 0, asm);
		if (breakends.size() == 0) {
			return true;
		}
		// too long
		int breakendLength = SAMRecordUtil.getSoftClipLength(asm, attr.getAssemblyDirection());
		if (breakendLength > ap.maxExpectedBreakendLengthMultiple * getMaxConcordantFragmentSize()) {
			log.debug(String.format("Filtering %s at %s:%d due to misassembly (breakend %dbp)",
					asm.getReadName(),
					asm.getReferenceName(), asm.getAlignmentStart(),
					breakendLength));
			return true;
		}
		// too few reads
		if (attr.getOriginatingFragmentID(null, null, null).size() < ap.minReads) {
			return true;
		}
		// unanchored assembly that not actually any longer than any of the reads that were assembled
		if (AssemblyAttributes.isUnanchored(asm) && breakendLength <= attr.getAssemblyMaxReadLength()) {
			// assembly length = 1 read
			// at best, we've just error corrected a single reads with other reads.
			// at worst, we've created a misassembly.
			return true;
		}
		return false;
	}
	public SAMRecord transformAssembly(SAMRecord assembly) {
		if (assembly.getReadUnmappedFlag()) return assembly;
		if (CigarUtil.widthOfImprecision(assembly.getCigar()) == 0) {
			// some assemblies actually match the reference and we can ignore these
			// such assemblies are caused by sequencing errors or SNVs causing
			// spurious soft clips
			SAMRecordUtil.unclipExactReferenceMatches(getContext().getReference(), assembly);
			/*
			float anchorMatchRate = SAMRecordUtil.getAlignedIdentity(assembly);
			if (anchorMatchRate < getContext().getAssemblyParameters().minAnchorIdentity) {
				SAMRecord realigned = SAMRecordUtil.realign(getContext().getReference(), assembly, getContext().getConfig().getAssembly().realignmentWindowSize, true);
				// TODO: check if realignment is actually better
				// (don't allow very short anchors)
				// (don't allow flipping anchor to other side)
				// ...
				float realignedAnchorMatchRate = SAMRecordUtil.getAlignedIdentity(realigned);
				if (realignedAnchorMatchRate >= getContext().getAssemblyParameters().minAnchorIdentity &&
						CigarUtil.commonReferenceBases(realigned.getCigar(), assembly.getCigar()) >= CigarUtil.countMappedBases(assembly.getCigar().getCigarElements()) * getContext().getConfig().getAssembly().minPortionOfAnchorRetained) {
					assembly = realigned;
				}
			}
			*/
		}
		return assembly;
	} 
	private Iterator<DirectedEvidence> throttled(Iterator<DirectedEvidence> it) {
		AssemblyConfiguration ap = getContext().getAssemblyParameters();
		DirectedEvidenceDensityThrottlingIterator dit = new DirectedEvidenceDensityThrottlingIterator(
				throttled,
				getContext().getDictionary(),
				getContext().getLinear(),
				it,
				Math.max(ap.downsampling.minimumDensityWindowSize, getMaxConcordantFragmentSize()),
				ap.downsampling.acceptDensityPortion * ap.downsampling.targetEvidenceDensity,
				ap.downsampling.targetEvidenceDensity);
		getContext().registerBuffer(AssemblyEvidenceSource.class.getName() + ".throttle", dit);
		return dit;
	}
	public int getMaxAssemblyLength() {
		// We could extract by generating metrics for the assembly
		float maxExpected = getContext().getAssemblyParameters().maxExpectedBreakendLengthMultiple * getMaxConcordantFragmentSize();
		maxExpected = Math.max(maxExpected, getContext().getAssemblyParameters().anchorLength);
		maxExpected *= 2; // anchor + breakend length
		return (int)maxExpected;
	}
	@Override 
	protected int getSortWindowSize() {
		// Worse case for assembly is worse than for the input BAMs as
		// assembly allows for unanchored split reads using the XNX notation
		int windowSize = super.getSortWindowSize() + getMaxConcordantFragmentSize();
		return windowSize;
	}
	private void invalidateSummaryCache() {
		cachedMaxSourceFragSize = -1;
		cachedMinConcordantFragmentSize = -1;
		cachedMaxReadLength = -1;
		cachedMaxReadMappedLength = -1;
	}
	private int calcMaxConcordantFragmentSize() {
		int fs = source.stream().mapToInt(s -> s.getMaxConcordantFragmentSize()).max().orElse(0);
		if (getFile().exists()) {
			fs = Math.max(super.getMaxConcordantFragmentSize(), fs);
		}
		return fs;
	}
	@Override
	public int getMaxConcordantFragmentSize() {
		if (cachedMaxSourceFragSize == -1) {
			cachedMaxSourceFragSize = calcMaxConcordantFragmentSize();
		}
		return cachedMaxSourceFragSize;
	}
	@Override
	public int getMinConcordantFragmentSize() {
		if (cachedMinConcordantFragmentSize == -1) {
			cachedMinConcordantFragmentSize = calcMinConcordantFragmentSize();
		}
		return cachedMinConcordantFragmentSize;
	}
	public int calcMinConcordantFragmentSize() {
		int minSourceFragSize = source.stream().mapToInt(s -> s.getMinConcordantFragmentSize()).min().orElse(0);
		if (getFile().exists()) {
			minSourceFragSize = Math.min(super.getMinConcordantFragmentSize(), minSourceFragSize);
		}
		return minSourceFragSize;
	}
	/**
	 * Maximum read length of reads contributing to assemblies
	 * @return
	 */
	@Override
	public int getMaxReadLength() {
		if (cachedMaxReadLength == -1) {
			cachedMaxReadLength = calcMaxReadLength();
		}
		return cachedMaxReadLength;
	}
	public int calcMaxReadLength() {
		int maxReadLength = source.stream().mapToInt(s -> s.getMaxReadLength()).max().orElse(0);
		if (getFile().exists()) {
			maxReadLength = Math.max(super.getMaxReadLength(), maxReadLength);
		}
		return maxReadLength;
	}
	@Override
	public int getMaxReadMappedLength() {
		if (cachedMaxReadMappedLength == -1) {
			cachedMaxReadMappedLength = calcMaxReadMappedLength();
		}
		return cachedMaxReadMappedLength;
	}
	public int calcMaxReadMappedLength() {
		int maxMappedReadLength = source.stream().mapToInt(s -> s.getMaxReadMappedLength()).max().orElse(0);
		if (getFile().exists()) {
			maxMappedReadLength = Math.max(super.getMaxReadMappedLength(), maxMappedReadLength);
		}
		return maxMappedReadLength;
	}
	@SuppressWarnings("unused")
	private Iterator<SAMRecord> getAllAssemblies_single_threaded() {
		ProgressLogger progressLog = new ProgressLogger(log);
		List<Iterator<SAMRecord>> list = new ArrayList<>();
		for (BreakendDirection direction : BreakendDirection.values()) {
			CloseableIterator<DirectedEvidence> it = mergedIterator(source, false);
			Iterator<DirectedEvidence> throttledIt = throttled(it);
			ProgressLoggingDirectedEvidenceIterator<DirectedEvidence> loggedIt = new ProgressLoggingDirectedEvidenceIterator<>(getContext(), throttledIt, progressLog);
			Iterator<SAMRecord> evidenceIt = new PositionalAssembler(getContext(), this, new SequentialIdGenerator("asm"), loggedIt, direction);
	    	list.add(evidenceIt);
		}
		return Iterators.concat(list.iterator());
	}
	/**
	 * Assembly contigs are not paired
	 */
	@Override
	public boolean knownSingleEnded() { return true; }
}
