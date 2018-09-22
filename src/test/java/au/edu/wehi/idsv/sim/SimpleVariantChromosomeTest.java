package au.edu.wehi.idsv.sim;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import au.edu.wehi.idsv.IdsvVariantContext;
import au.edu.wehi.idsv.IntermediateFilesTest;
import au.edu.wehi.idsv.vcf.SvType;


public class SimpleVariantChromosomeTest extends IntermediateFilesTest {
	@Test
	public void should_space_variants_with_margin_padding() throws IOException {
		SimpleVariantChromosome svc = new SimpleVariantChromosome(getContext(), "polyACGT", 1, 0);
		svc.assemble(input, output, false, Lists.newArrayList(SvType.DEL, SvType.INS, SvType.INV, SvType.DUP), Lists.newArrayList(2), 1);
		// 123456  78901234  5
		// ACGTAC  GTACGTAC  G
		// ||  ||  ||  ||    |
		// AC--ACNNGTGTGTACACG
		//  *|| *|| *|| *||||
		//   v1  v2  v3   v4
		List<IdsvVariantContext> vcf = super.getVcf(output, null);
		assertEquals(2, vcf.get(0).getStart());
		assertEquals(6, vcf.get(1).getStart());
		assertEquals(8, vcf.get(2).getStart());
		assertEquals(12, vcf.get(3).getStart());
	}
	@Test
	public void should_output_variant_sequence() throws IOException {
		SimpleVariantChromosome svc = new SimpleVariantChromosome(getContext(), "polyACGT", 1, 0);
		svc.assemble(input, output, false, Lists.newArrayList(SvType.DEL, SvType.INS, SvType.INV, SvType.DUP), Lists.newArrayList(2), 1);
		// 123456  78901234  5
		// ACGTAC  GTACGTAC  G
		// ||  ||  ||  ||    |
		// AC--ACNNGTGTGTACACG
		//  *|| *|| *|| *||||
		//   v1  v2  v3   v4
		List<String> fa = Files.readLines(input, StandardCharsets.US_ASCII);
		assertEquals("ACACCGGTGTGTACACG", fa.get(1));
	}
	@Test
	public void should_output_corresponding_reference_sequences_only() throws IOException {
		SimpleVariantChromosome svc = new SimpleVariantChromosome(getContext(), "polyACGT", 1, 0);
		svc.assemble(input, output, true, Lists.newArrayList(SvType.DEL, SvType.INS, SvType.INV, SvType.DUP), Lists.newArrayList(2), 1);
		// 123456  78901234  5
		// ACGTAC  GTACGTAC  G
		// ||  ||  ||  ||    |
		// AC--ACNNGTGTGTACACG
		//  *|| *|| *|| *||||
		//   v1  v2  v3   v4
		List<String> fa = Files.readLines(input, StandardCharsets.US_ASCII);
		assertEquals(15, fa.get(3).length());
	}
	@Test
	public void shouldCalculateMicrohomologyLength() throws IOException {
		// 12345678901234567890
		// NNANAAANAAAAAAANAAAAAAAAAAAAAAANAAAAAAAAAAAAAAAAAAAAAAAAAAA
		//           *|      *|
		//           v1      v2
		SimpleVariantChromosome svc = new SimpleVariantChromosome(getContext(), "Npower2", 2, 0);
		svc.assemble(input, output, false, Lists.newArrayList(SvType.DEL), Lists.newArrayList(1), 5);
		List<IdsvVariantContext> vcf = super.getVcf(output, null);
		assertEquals("6", vcf.get(0).getAttribute("HOMLEN"));
		assertEquals(11, vcf.get(0).getStart());
		assertEquals(Lists.newArrayList("-3", "3"), vcf.get(0).getAttribute("CIPOS"));
		assertEquals(19, vcf.get(1).getStart());
		assertEquals("14", vcf.get(1).getAttribute("HOMLEN"));
		assertEquals(Lists.newArrayList("-3", "11"), vcf.get(1).getAttribute("CIPOS"));
	}
	@Test
	public void shouldFilterWhenVariantMatchesReferenceSequence() throws IOException {
		// 12345678
		// ACGTACGT
		//   *||
		SimpleVariantChromosome svc = new SimpleVariantChromosome(getContext(), "polyACGT", 2, 0);
		svc.assemble(input, output, false, Lists.newArrayList(SvType.INV), Lists.newArrayList(2), 1);
		List<IdsvVariantContext> vcf = super.getVcf(output, null);
		assertEquals(3, vcf.get(0).getStart());
		assertTrue(vcf.get(0).getFilters().contains("REF"));
	}
}
