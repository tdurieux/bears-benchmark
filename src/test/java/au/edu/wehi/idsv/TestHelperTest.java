package au.edu.wehi.idsv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class TestHelperTest extends TestHelper {
	@Test
	public void test_getRef() {
		assertEquals("ATTAA", S(getRef(RANDOM, 5, 6, FWD)));
		assertEquals("CTGAA", S(getRef(RANDOM, 5, 125, BWD)));
	}
	@Test
	public void test_getBasePos() {
		assertEquals(1, getLeftmostBasePos(1, FWD, 1, 0));
		
		assertEquals(10, getLeftmostBasePos(10, FWD, 1, 0));
		assertEquals(9, getLeftmostBasePos(10, FWD, 2, 0));
		assertEquals(8, getLeftmostBasePos(10, FWD, 3, 0));
		
		assertEquals(9, getLeftmostBasePos(10, FWD, 1, 1));
		assertEquals(8, getLeftmostBasePos(10, FWD, 2, 1));
		assertEquals(7, getLeftmostBasePos(10, FWD, 3, 1));
		
		assertEquals(10, getLeftmostBasePos(10, BWD, 1, 0));
		assertEquals(10, getLeftmostBasePos(10, BWD, 2, 0));
		assertEquals(10, getLeftmostBasePos(10, BWD, 3, 0));
		
		assertEquals(11, getLeftmostBasePos(10, BWD, 1, 1));
		assertEquals(11, getLeftmostBasePos(10, BWD, 2, 1));
		assertEquals(11, getLeftmostBasePos(10, BWD, 3, 1));
	}
	@Test
	public void SES_category() {
		assertEquals(0, SES(false).getSourceCategory());
		assertEquals(1, SES(true).getSourceCategory());
	}
}

