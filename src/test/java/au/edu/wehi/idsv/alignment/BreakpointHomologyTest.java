package au.edu.wehi.idsv.alignment;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import au.edu.wehi.idsv.BreakendDirection;
import au.edu.wehi.idsv.BreakpointSummary;
import au.edu.wehi.idsv.TestHelper;
import au.edu.wehi.idsv.picard.InMemoryReferenceSequenceFile;
import au.edu.wehi.idsv.picard.TwoBitBufferedReferenceSequenceFile;


public class BreakpointHomologyTest extends TestHelper {
	@Test(expected=IllegalArgumentException.class)
	public void should_require_exact_local_breakpoint() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCAATGGGCCC"),
							   B("TTTAATGGGAAA"), });
								//     >
								//      <
		BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 6, 6, 7, 1, BWD, 7, 7, 7), "", 100, 0);
	}
	@Test(expected=IllegalArgumentException.class)
	public void should_require_exact_remote_breakpoint() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCAATGGGCCC"),
							   B("TTTAATGGGAAA"), });
								//     >
								//      <
		BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 6, 6, 6, 1, BWD, 7, 7, 8), "", 100, 0);
	}
	@Test
	public void should_report_homology_on_both_sides() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCAATGGGCCC"),
							   B("TTTAATGGGAAA"), });
								//123456789012
								//     >
								//      <
		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 6, 1, BWD, 7), "", 100, 0);
		assertEquals(6, bh.getRemoteHomologyLength() + bh.getLocalHomologyLength());
		assertEquals(3, bh.getLocalHomologyLength());
		assertEquals(3, bh.getRemoteHomologyLength());
	}
	@Test
	public void no_homology() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCCCCCCCCCC"),
							   B("TTTTTTTTTTTTTTTTTTTTTTTT"), });
								//     >
								//      <
		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 6, 1, BWD, 7), "", 100, 10);
		assertEquals(0, bh.getLocalHomologyLength());
		assertEquals(0, bh.getRemoteHomologyLength());
	}
	@Test
	public void should_report_homology_up_to_max_distance_away() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCAATGGGCCCTTTTTTTTTTTTTTTTTTT"),
							   B("TTTAATGGGAAAGGGGGGGGGGGGGGGGGGG"), });
								//     >
								//      <
		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 6, 1, BWD, 7), "", 1, 5);
		assertEquals(1, bh.getLocalHomologyLength());
		assertEquals(1, bh.getRemoteHomologyLength());
	}
	@Test
	public void should_reduce_window_size_for_small_events() {
		BreakpointHomology bh = BreakpointHomology.calculate(SMALL_FA, new BreakpointSummary(0, FWD, 100, 0, BWD, 103), "", 100, 0);
		assertEquals(2, bh.getLocalHomologyLength());
		assertEquals(2, bh.getRemoteHomologyLength());
	}
	@Test
	public void should_incorporate_inserted_sequence() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCAAAATTTGGGAAAAAATTTTTTTTTTTTTTTTTTT"),
							   B("TTTAAAATTTGGGAAAAAAGGGGGGGGGGGGGGGGGGG"), });
		// CCCAAAATTT GGGAAAAAATTTTTTTTTTT TTTTTTTT"),
		// TTTAAAATTT GGGAAAAAAGGGGGGGGGGG GGGGGGGG"
		// MMMMMMMMMMIMMMMMMMMSSSSSSSSSSSS fwd
		// SSSMMMMMMMIMMMMMMMMMMMMMMMMMMMM bwd
		// 1234567890 1234567890
		//          >G
		//            <
		// 
		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 10, 1, BWD, 11), "c", 20, 10);
		assertEquals(7, bh.getLocalHomologyLength());
		assertEquals(9, bh.getRemoteHomologyLength());
	}
	@Test
	public void should_not_exceed_contig_bounds() {
		InMemoryReferenceSequenceFile underlying = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] { B("CCCCCCCCCCC"),
							   B("AAAAAAAAAAA"), });
		//BreakpointHomology bh0 = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 1, 1, BWD, 20), "", 50, 50);
		TwoBitBufferedReferenceSequenceFile ref = new TwoBitBufferedReferenceSequenceFile(underlying);
		for (int b1pos = 1; b1pos <= ref.getSequenceDictionary().getSequences().get(0).getSequenceLength(); b1pos++) {
			for (int b2pos = 1; b2pos <= ref.getSequenceDictionary().getSequences().get(0).getSequenceLength(); b2pos++) {
				for (BreakendDirection b1dir : BreakendDirection.values()) {
					for (BreakendDirection b2dir : BreakendDirection.values()) {
						for (int maxBreakendLength = 1; maxBreakendLength < ref.getSequenceDictionary().getSequences().get(0).getSequenceLength() + 2; maxBreakendLength++) {
							for (int margin = 0; margin < ref.getSequenceDictionary().getSequences().get(0).getSequenceLength() + 2; margin++) {
								BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, b1dir, b1pos, 1, b2dir, b2pos), "", maxBreakendLength, margin);
								assertEquals(0, bh.getLocalHomologyLength());
								assertEquals(0, bh.getRemoteHomologyLength());
							}
						}
					}
				}
			}
		}
	}
	@Test
	public void should_not_report_homology_for_small_dup() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] {
						//    0        1         2         3         4         5         6
						//    123456789012345678901234567890123456789012345678901234567890
						B("CATTAATCGCAAGAGCGGGTTGTATTCGACGCCAAGTCAGCTGAAGCACCATTACC"),
						B("CACCGCCTATTCGAACGGGCGAATCTACCTAGGTCGCTCAGAACCGGCACCCTTAA"), });

		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, BWD, 10, 0, FWD, 20), "", 20, 10);
		assertEquals(0, bh.getLocalHomologyLength());
		assertEquals(0, bh.getRemoteHomologyLength());
	}
	@Test
	public void should_handle_inserted_sequence_no_homology() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] {
						//    0        1         2         3         4         5         6
						//    123456789012345678901234567890123456789012345678901234567890
						B("CATTAATCGCAAGAGCGGGTTGTATTCGACGCCAAGTCAGCTGAAGCACCATTACC"),
						B("CACCGCCTATTCGAACGGGCGAATCTACCTAGGTCGCTCAGAACCGGCACCCTTAA"), });

		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 10, 1, BWD, 10), "GGGGG", 20, 10);
		assertEquals(0, bh.getLocalHomologyLength());
		assertEquals(0, bh.getRemoteHomologyLength());
	}
	@Test
	public void should_handle_inserted_sequence_with_homology() {
		InMemoryReferenceSequenceFile ref = new InMemoryReferenceSequenceFile(
				new String[] { "0", "1", },
				new byte[][] {
						//    0        1         2         3         4         5         6
						//    123456789012345678901234567890123456789012345678901234567890
						B("AGAACCGGCACCCTTAACATTAATCGCAAGAGCGGGTTGTATTCGACGCCAAGTCAGCTGAAGCACCATTACC"),
						B("AGAACCGGCACCCTTAACACCGCCTACAAGAGCGGGTTGTATTCGACGCCAAGTCAGCTGAAGCACCATTACC"), });

		BreakpointHomology bh = BreakpointHomology.calculate(ref, new BreakpointSummary(0, FWD, 27, 1, BWD, 28), "GGGGG", 20, 5);
		assertEquals(0, bh.getLocalHomologyLength());
		assertEquals(20, bh.getRemoteHomologyLength());
	}
}
