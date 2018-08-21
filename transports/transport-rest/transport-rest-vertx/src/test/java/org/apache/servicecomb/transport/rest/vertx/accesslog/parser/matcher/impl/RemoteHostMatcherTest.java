/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.servicecomb.transport.rest.vertx.accesslog.parser.matcher.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.servicecomb.transport.rest.vertx.accesslog.element.impl.RemoteHostElement;
import org.apache.servicecomb.transport.rest.vertx.accesslog.parser.AccessLogElementExtraction;
import org.junit.Test;

public class RemoteHostMatcherTest {
  private static final RemoteHostMatcher MATCHER = new RemoteHostMatcher();

  private static final String RAW_PATTERN = "%h %h %{PATTERN}t %h%h %H %h";

  @Test
  public void testExtractElementPlaceHolder() {
    List<AccessLogElementExtraction> extractionList = MATCHER.extractElementPlaceholder(RAW_PATTERN);

    assertEquals(5, extractionList.size());
    assertEquals(0, extractionList.get(0).getStart());
    assertEquals(2, extractionList.get(0).getEnd());
    assertEquals(MATCHER.getAccessLogElement(), extractionList.get(0).getAccessLogElement());
    assertEquals(3, extractionList.get(1).getStart());
    assertEquals(5, extractionList.get(1).getEnd());
    assertEquals(MATCHER.getAccessLogElement(), extractionList.get(1).getAccessLogElement());
    assertEquals(18, extractionList.get(2).getStart());
    assertEquals(20, extractionList.get(2).getEnd());
    assertEquals(MATCHER.getAccessLogElement(), extractionList.get(2).getAccessLogElement());
    assertEquals(20, extractionList.get(3).getStart());
    assertEquals(22, extractionList.get(3).getEnd());
    assertEquals(MATCHER.getAccessLogElement(), extractionList.get(3).getAccessLogElement());
    assertEquals(26, extractionList.get(4).getStart());
    assertEquals(28, extractionList.get(4).getEnd());
    assertEquals(MATCHER.getAccessLogElement(), extractionList.get(4).getAccessLogElement());
  }

  @Test
  public void testGetPlaceholderPattern() {
    assertEquals("%h", MATCHER.getPlaceholderPattern());
  }

  @Test
  public void getAccessLogElement() {
    assertTrue(RemoteHostElement.class.equals(MATCHER.getAccessLogElement().getClass()));
  }
}
