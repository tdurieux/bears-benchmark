package au.edu.wehi.idsv.bed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import org.junit.Test;

import au.edu.wehi.idsv.BreakpointSummary;
import au.edu.wehi.idsv.IntermediateFilesTest;


public class BedpeWriterTest extends IntermediateFilesTest {
	@Test
	public void should_write_headers() throws IOException {
		BedpeWriter w = new BedpeWriter(getSequenceDictionary(), output);
		w.writeHeader(false, true);
		w.close();
		List<String> out = Files.readAllLines(output.toPath(), StandardCharsets.US_ASCII);
		
		assertEquals(1, out.size());
		assertTrue(out.get(0).contains("AS"));
		assertTrue(out.get(0).contains("RAS"));
	}
	@Test
	public void should_write_one_line_per_record() throws IOException {
		BedpeWriter w = new BedpeWriter(getSequenceDictionary(), output);
		w.writeHeader(false, true);
		w.write(BP("id", new BreakpointSummary(0, FWD, 2, 1, 2, 3, BWD, 5, 4, 5)));
		w.close();
		List<String> out = Files.readAllLines(output.toPath(), StandardCharsets.US_ASCII);
		
		assertEquals(2, out.size());
		assertTrue(out.get(1).contains("id"));
		assertEquals("polyA", out.get(1).split("	")[0]);
		assertEquals("0", out.get(1).split("	")[1]);
		assertEquals("2", out.get(1).split("	")[2]);
	}
	@Test
	public void should_use_zero_based_half_open_intervals() throws IOException {
		BedpeWriter w = new BedpeWriter(getSequenceDictionary(), output);
		w.write(BP("id", new BreakpointSummary(0, FWD, 2, 1, 2, 3, BWD, 5, 4, 5)));
		w.close();
		String[] out = Files.readAllLines(output.toPath(), StandardCharsets.US_ASCII).get(0).split("	");
		
		assertEquals("polyA", out[0]);
		assertEquals("0", out[1]);
		assertEquals("2", out[2]);
		assertEquals("Npower2", out[3]);
		assertEquals("3", out[4]);
		assertEquals("5", out[5]);
		assertEquals("id", out[6]);
		//assertEquals("0", out[7]);
		assertEquals("+", out[8]);
		assertEquals("-", out[9]);
	}
}
