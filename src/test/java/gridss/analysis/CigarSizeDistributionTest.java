package gridss.analysis;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import htsjdk.samtools.CigarOperator;


public class CigarSizeDistributionTest {
	public static List<CigarDetailMetrics> data_778() {
		List<CigarDetailMetrics> list = new ArrayList<CigarDetailMetrics>();
		list.add(new CigarDetailMetrics() {{ LENGTH = 0; COUNT = 105662867; OPERATOR='S'; }});
		list.add(new CigarDetailMetrics() {{ LENGTH = 1; COUNT = 1267368; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 2; COUNT = 828481; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 3; COUNT = 428075; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 4; COUNT = 225398; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 5; COUNT = 148866; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 6; COUNT = 133058; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 7; COUNT = 115614; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 8; COUNT = 93541; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 9; COUNT = 82310; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 10; COUNT = 77553; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 11; COUNT = 64227; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 12; COUNT = 56625; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 13; COUNT = 52518; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 14; COUNT = 48524; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 15; COUNT = 43497; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 16; COUNT = 39685; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 17; COUNT = 37281; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 18; COUNT = 34535; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 19; COUNT = 34209; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 20; COUNT = 31666; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 21; COUNT = 25670; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 22; COUNT = 23803; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 23; COUNT = 22230; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 24; COUNT = 20731; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 25; COUNT = 19441; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 26; COUNT = 18415; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 27; COUNT = 17802; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 28; COUNT = 16686; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 29; COUNT = 16611; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 30; COUNT = 16525; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 31; COUNT = 13138; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 32; COUNT = 12664; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 33; COUNT = 11521; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 34; COUNT = 11163; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 35; COUNT = 10792; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 36; COUNT = 10576; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 37; COUNT = 10516; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 38; COUNT = 10124; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 39; COUNT = 10182; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 40; COUNT = 10699; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 41; COUNT = 7914; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 42; COUNT = 7901; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 43; COUNT = 7330; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 44; COUNT = 7199; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 45; COUNT = 7004; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 46; COUNT = 6770; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 47; COUNT = 7043; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 48; COUNT = 7008; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 49; COUNT = 7057; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 50; COUNT = 7417; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 51; COUNT = 5081; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 52; COUNT = 4821; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 53; COUNT = 4556; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 54; COUNT = 4608; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 55; COUNT = 4487; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 56; COUNT = 4491; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 57; COUNT = 4635; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 58; COUNT = 4733; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 59; COUNT = 4702; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 60; COUNT = 5328; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 61; COUNT = 3158; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 62; COUNT = 3102; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 63; COUNT = 3105; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 64; COUNT = 2906; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 65; COUNT = 2794; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 66; COUNT = 2904; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 67; COUNT = 2993; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 68; COUNT = 3043; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 69; COUNT = 3263; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 70; COUNT = 3534; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 71; COUNT = 1674; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 72; COUNT = 1532; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 73; COUNT = 1464; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 74; COUNT = 1211; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 75; COUNT = 1319; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 76; COUNT = 1265; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 77; COUNT = 1541; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 78; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 79; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 80; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 81; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 82; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 83; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 84; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 85; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 86; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 87; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 88; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 89; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 90; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 91; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 92; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 93; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 94; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 95; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 96; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 97; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 98; COUNT = 0; OPERATOR='S';}});
		list.add(new CigarDetailMetrics() {{ LENGTH = 99; COUNT = 0; OPERATOR='S';}});
		return list;
	}
	@Test
	public void should_calc_phred_score() {
		CigarSizeDistribution d = new CigarSizeDistribution(data_778());
		assertEquals(0, d.getPhred(CigarOperator.S, 0), 0.001);
		assertEquals(14.0649733007887, d.getPhred(CigarOperator.S, 1), 0.001);
		assertEquals(15.5759043682811, d.getPhred(CigarOperator.S, 2), 0.001);
		assertEquals(16.9546124331507, d.getPhred(CigarOperator.S, 3), 0.001);
		assertEquals(17.8861774354832, d.getPhred(CigarOperator.S, 4), 0.001);
		assertEquals(18.4709175523681, d.getPhred(CigarOperator.S, 5), 0.001);
		assertEquals(18.9053412273954, d.getPhred(CigarOperator.S, 6), 0.001);
		assertEquals(19.3342138972767, d.getPhred(CigarOperator.S, 7), 0.001);
		assertEquals(19.7446822482152, d.getPhred(CigarOperator.S, 8), 0.001);
		assertEquals(20.1077461288297, d.getPhred(CigarOperator.S, 9), 0.001);
		assertEquals(20.4544287878607, d.getPhred(CigarOperator.S, 10), 0.001);
		assertEquals(20.8085171832688, d.getPhred(CigarOperator.S, 11), 0.001);
		assertEquals(21.1253277576786, d.getPhred(CigarOperator.S, 12), 0.001);
		assertEquals(21.4251986713991, d.getPhred(CigarOperator.S, 13), 0.001);
		assertEquals(21.7231366450605, d.getPhred(CigarOperator.S, 14), 0.001);
		assertEquals(22.0178562200273, d.getPhred(CigarOperator.S, 15), 0.001);
		assertEquals(22.3001956251441, d.getPhred(CigarOperator.S, 16), 0.001);
		assertEquals(22.5748538189961, d.getPhred(CigarOperator.S, 17), 0.001);
		assertEquals(22.8497255725281, d.getPhred(CigarOperator.S, 18), 0.001);
		assertEquals(23.1208726404096, d.getPhred(CigarOperator.S, 19), 0.001);
		assertEquals(23.4072603787115, d.getPhred(CigarOperator.S, 20), 0.001);
		assertEquals(23.6903223658872, d.getPhred(CigarOperator.S, 21), 0.001);
		assertEquals(23.9341474594603, d.getPhred(CigarOperator.S, 22), 0.001);
		assertEquals(24.1731643283622, d.getPhred(CigarOperator.S, 23), 0.001);
		assertEquals(24.4089279673423, d.getPhred(CigarOperator.S, 24), 0.001);
		assertEquals(24.6409606194963, d.getPhred(CigarOperator.S, 25), 0.001);
		assertEquals(24.8704294281474, d.getPhred(CigarOperator.S, 26), 0.001);
		assertEquals(25.0995729281676, d.getPhred(CigarOperator.S, 27), 0.001);
		assertEquals(25.33320994188, d.getPhred(CigarOperator.S, 28), 0.001);
		assertEquals(25.5642351469091, d.getPhred(CigarOperator.S, 29), 0.001);
		assertEquals(25.8071157333773, d.getPhred(CigarOperator.S, 30), 0.001);
		assertEquals(26.0630163441606, d.getPhred(CigarOperator.S, 31), 0.001);
		assertEquals(26.2778054053944, d.getPhred(CigarOperator.S, 32), 0.001);
		assertEquals(26.4954121878496, d.getPhred(CigarOperator.S, 33), 0.001);
		assertEquals(26.7033202859339, d.getPhred(CigarOperator.S, 34), 0.001);
		assertEquals(26.9147308464297, d.getPhred(CigarOperator.S, 35), 0.001);
		assertEquals(27.1293902203165, d.getPhred(CigarOperator.S, 36), 0.001);
		assertEquals(27.3505768831843, d.getPhred(CigarOperator.S, 37), 0.001);
		assertEquals(27.5822778712585, d.getPhred(CigarOperator.S, 38), 0.001);
		assertEquals(27.817664714572, d.getPhred(CigarOperator.S, 39), 0.001);
		assertEquals(28.0680119936773, d.getPhred(CigarOperator.S, 40), 0.001);
		assertEquals(28.3476108599568, d.getPhred(CigarOperator.S, 41), 0.001);
		assertEquals(28.5666641789958, d.getPhred(CigarOperator.S, 42), 0.001);
		assertEquals(28.7969667567788, d.getPhred(CigarOperator.S, 43), 0.001);
		assertEquals(29.0221293364742, d.getPhred(CigarOperator.S, 44), 0.001);
		assertEquals(29.2552468608571, d.getPhred(CigarOperator.S, 45), 0.001);
		assertEquals(29.494730618539, d.getPhred(CigarOperator.S, 46), 0.001);
		assertEquals(29.7394835961963, d.getPhred(CigarOperator.S, 47), 0.001);
		assertEquals(30.0096494075913, d.getPhred(CigarOperator.S, 48), 0.001);
		assertEquals(30.2962631340505, d.getPhred(CigarOperator.S, 49), 0.001);
		assertEquals(30.6053608034657, d.getPhred(CigarOperator.S, 50), 0.001);
		assertEquals(30.9558352151959, d.getPhred(CigarOperator.S, 51), 0.001);
		assertEquals(31.2133675589591, d.getPhred(CigarOperator.S, 52), 0.001);
		assertEquals(31.4727032092736, d.getPhred(CigarOperator.S, 53), 0.001);
		assertEquals(31.7328895430087, d.getPhred(CigarOperator.S, 54), 0.001);
		assertEquals(32.0129256764225, d.getPhred(CigarOperator.S, 55), 0.001);
		assertEquals(32.3041409267973, d.getPhred(CigarOperator.S, 56), 0.001);
		assertEquals(32.6165846131424, d.getPhred(CigarOperator.S, 57), 0.001);
		assertEquals(32.9645002359569, d.getPhred(CigarOperator.S, 58), 0.001);
		assertEquals(33.3510977878893, d.getPhred(CigarOperator.S, 59), 0.001);
		assertEquals(33.7725829717661, d.getPhred(CigarOperator.S, 60), 0.001);
		assertEquals(34.3055292505632, d.getPhred(CigarOperator.S, 61), 0.001);
		assertEquals(34.6553325504273, d.getPhred(CigarOperator.S, 62), 0.001);
		assertEquals(35.0287532465374, d.getPhred(CigarOperator.S, 63), 0.001);
		assertEquals(35.4377425987591, d.getPhred(CigarOperator.S, 64), 0.001);
		assertEquals(35.8588992033717, d.getPhred(CigarOperator.S, 65), 0.001);
		assertEquals(36.3063907888808, d.getPhred(CigarOperator.S, 66), 0.001);
		assertEquals(36.8262115106648, d.getPhred(CigarOperator.S, 67), 0.001);
		assertEquals(37.4362524856, d.getPhred(CigarOperator.S, 68), 0.001);
		assertEquals(38.1591140819105, d.getPhred(CigarOperator.S, 69), 0.001);
		assertEquals(39.0967957123033, d.getPhred(CigarOperator.S, 70), 0.001);
		assertEquals(40.4103773703206, d.getPhred(CigarOperator.S, 71), 0.001);
		assertEquals(41.2054897430374, d.getPhred(CigarOperator.S, 72), 0.001);
		assertEquals(42.0878932287322, d.getPhred(CigarOperator.S, 73), 0.001);
		assertEquals(43.1408241467096, d.getPhred(CigarOperator.S, 74), 0.001);
		assertEquals(44.2587428269351, d.getPhred(CigarOperator.S, 75), 0.001);
		assertEquals(45.9321056888711, d.getPhred(CigarOperator.S, 76), 0.001);
		assertEquals(48.5349559686103, d.getPhred(CigarOperator.S, 77), 0.001);
	}
	@Test
	public void should_cap_at_boundry_phred_score() {
		CigarSizeDistribution d = new CigarSizeDistribution(data_778());
		assertEquals(0, d.getPhred(CigarOperator.S, -2), 0.001);
		assertEquals(0, d.getPhred(CigarOperator.S, -1), 0.001);
		assertEquals(0, d.getPhred(CigarOperator.S, 0), 0.001);
		assertEquals(48.5349559686103, d.getPhred(CigarOperator.S,77), 0.001);
		assertEquals(48.5349559686103, d.getPhred(CigarOperator.S,78), 0.001);
		assertEquals(48.5349559686103, d.getPhred(CigarOperator.S,79), 0.001);
		assertEquals(48.5349559686103, d.getPhred(CigarOperator.S,1000), 0.001);
	}
	@Test
	public void should_default_to_zero_score() {
		CigarSizeDistribution d = new CigarSizeDistribution(new ArrayList<CigarDetailMetrics>());
		assertEquals(0, d.getPhred(CigarOperator.S, 0), 0);
	}
}
