package com.github.mforoni.jcoder;

import com.github.mforoni.jcoder.Names;
import junit.framework.TestCase;

public class NamesTest extends TestCase {

	public final void testOfField() {
		assertEquals("test", Names.ofField(" Test"));
		assertEquals("teST", Names.ofField(" TeST"));
		assertEquals("tEST", Names.ofField(" TEST"));
		assertEquals("veryLongTextToBeConverted", Names.ofField(" veRY Long texT_to Be ConVERTED"));
	}
}
