package au.edu.wehi.idsv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.google.common.util.concurrent.MoreExecutors;

import au.edu.wehi.idsv.util.AsyncBufferedIterator;
import au.edu.wehi.idsv.util.FileHelper;
import au.edu.wehi.idsv.vcf.VcfFileUtil;
import htsjdk.samtools.QueryInterval;
import htsjdk.samtools.util.Log;
import htsjdk.variant.variantcontext.writer.VariantContextWriter;


/**
 * Calls structural variant
 * @author Daniel Cameron
 *
 */
public class VariantCaller {
	private static final Log log = Log.getInstance(VariantCaller.class);
	private final ProcessingContext processContext;
	private final List<SAMEvidenceSource> samEvidence;
	private final AssemblyEvidenceSource assemblyEvidence;
	//private final EvidenceToCsv evidenceDump;
	public VariantCaller(ProcessingContext context, List<SAMEvidenceSource> samEvidence, AssemblyEvidenceSource assemblyEvidence) {
		this.processContext = context;
		this.samEvidence = samEvidence;
		this.assemblyEvidence = assemblyEvidence;
	}
	public void callBreakends(File vcf, ExecutorService threadpool) throws IOException {
		samEvidence.stream().forEach(ses -> ses.assertPreprocessingComplete());
		assemblyEvidence.assertPreprocessingComplete();
		log.info("Identifying Breakpoints");
		if (threadpool == null) {
			threadpool = MoreExecutors.newDirectExecutorService();
		}
		AggregateEvidenceSource es = new AggregateEvidenceSource(processContext, samEvidence, assemblyEvidence);
		List<QueryInterval[]> chunks = processContext.getReference().getIntervals(processContext.getConfig().chunkSize, processContext.getConfig().chunkSequenceChangePenalty);
		List<File> calledChunk = new ArrayList<>();
		List<Future<Void>> tasks = new ArrayList<>();
		
		for (int i = 0; i < chunks.size(); i++) {
			QueryInterval[] chunck = chunks.get(i);
			File f = processContext.getFileSystemContext().getVariantCallChunkVcf(vcf, i);
			int chunkNumber = i;
			calledChunk.add(f);
			if (!f.exists()) {
				tasks.add(threadpool.submit(() -> { callChunk(f, es, chunkNumber, chunck); return null; }));
			}
		}
		runTasks(tasks);
		
		log.info("Merging identified breakpoints");
		File mergedOut = FileSystemContext.getWorkingFileFor(vcf, "gridss.merged.");
		VcfFileUtil.concat(processContext.getReference().getSequenceDictionary(), calledChunk, mergedOut);
		
		log.info("Sorting identified breakpoints");
		VcfFileUtil.sort(processContext, mergedOut, vcf);
		// clean up chunked
		if (gridss.Defaults.DELETE_TEMPORARY_FILES) {
			for (File f : calledChunk) {
				FileHelper.delete(f, true);
			}
			FileHelper.delete(mergedOut, true);
		}
	}
	private void runTasks(List<Future<Void>> tasks) {
		// Run as many tasks as we can before dying
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
		if (firstException != null) {
			log.error(firstException, "Fatal error during breakpoint identification ");
			throw new RuntimeException(firstException);
		}
	}
	private void callChunk(File output, AggregateEvidenceSource es, int chunkNumber, QueryInterval[] chunk) {
		String chunkMsg = String.format("chunk %d (%s:%d-%s:%d)", chunkNumber,
				processContext.getDictionary().getSequence(chunk[0].referenceIndex).getSequenceName(), chunk[0].start,
				processContext.getDictionary().getSequence(chunk[chunk.length-1].referenceIndex).getSequenceName(), chunk[chunk.length-1].end);
		String msg = "calling maximal cliques in " + chunkMsg;
		File tmp = gridss.Defaults.OUTPUT_TO_TEMP_FILE ? FileSystemContext.getWorkingFileFor(output) : output;
		try (VariantCallIterator rawit = new VariantCallIterator(es, chunk, chunkNumber)) {
			try (VariantContextWriter vcfWriter = processContext.getVariantContextWriter(tmp, false)) {
				log.info("Start ", msg);
				try (AsyncBufferedIterator<VariantContextDirectedEvidence> it = new AsyncBufferedIterator<>(rawit, "VariantCaller " + chunkMsg)) {
					while (it.hasNext()) {
						VariantContextDirectedEvidence loc = it.next();
						if (loc.getBreakendQual() >= processContext.getVariantCallingParameters().minScore || processContext.getVariantCallingParameters().writeFiltered) {
							// If we're under min score with all possible evidence allocated, we're definitely going to fail
							// when we restrict evidence to single breakpoint support
							vcfWriter.add(loc);
						}
					}
				}
			}
		}
		try {
			if (tmp != output) {
				FileHelper.move(tmp, output, true);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		log.info("Complete ", msg);
		if (gridss.Defaults.DELETE_TEMPORARY_FILES) {
			try {
				if (tmp != output) {
					FileHelper.delete(tmp, true);
				}
			} catch (IOException e) {
				log.warn(e, "Error removing intermediate file ", tmp.getAbsolutePath());
			}
		}
		if (gridss.Defaults.DEFENSIVE_GC) {
			log.debug("Requesting defensive GC to ensure OS file handles are closed");
			System.gc();
			System.runFinalization();
		}
	}
}
