package com.github.mforoni.jcoder;

import junit.framework.TestCase;

public class NamesTest extends TestCase {
  public final void testOfField() {
    assertEquals("test", Names.ofField(" TeST"));
    assertEquals("test", Names.ofField(" 1_TEST"));
    assertEquals("veryLongTextToBeConverted", Names.ofField(" veRY Long texT_to Be ConVERTED"));
    assertEquals("veryLongTextToBeConverted", Names.ofField(" veRY . Long texT_ to Be ConVERTED"));
  }
}
