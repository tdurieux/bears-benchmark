/* LanguageTool, a natural language style checker
 * Copyright (C) 2005 Daniel Naber (http://www.danielnaber.de)
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301
 * USA
 */
package org.languagetool.tokenizers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.languagetool.tools.StringTools;

/**
 * Tokenizes a sentence into words. Punctuation and whitespace gets their own tokens.
 * The tokenizer is a quite simple character-based one, though it knows
 * about urls and will put them in one token, if fully specified including
 * a protocol (like {@code http://foobar.org}).
 * 
 * @author Daniel Naber
 */
public class WordTokenizer implements Tokenizer {

  private static final List<String> PROTOCOLS = Collections.unmodifiableList(Arrays.asList("http", "https", "ftp"));
  private static final Pattern URL_CHARS = Pattern.compile("[a-zA-Z0-9/%$-_.+!*'(),\\?]+");
  private static final Pattern E_MAIL = Pattern.compile("(?<!:)\\b[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))\\b");
  private static final Pattern ONE_WORD_TWO_DOT_PATTERN = Pattern.compile("[ ,]?[0-9a-zA-Z]+[\\.][0-9a-zA-Z]+[\\.][0-9a-zA-Z]+");

  private static final String TOKENIZING_CHARACTERS = "\u0020\u00A0\u115f" +
      "\u1160\u1680"
      + "\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007"
      + "\u2008\u2009\u200A\u200B\u200c\u200d\u200e\u200f"
      + "\u2028\u2029\u202a\u202b\u202c\u202d\u202e\u202f"
      + "\u205F\u2060\u2061\u2062\u2063\u206A\u206b\u206c\u206d"
      + "\u206E\u206F\u3000\u3164\ufeff\uffa0\ufff9\ufffa\ufffb"
      + ",.;()[]{}=*#∗×·+÷<>!?:/|\\\"'«»„”“`´‘’‛′…¿¡→‼⁇⁈⁉"
      + "—"  // em dash
      + "\t\n\r";

  /**
   * Get the protocols that the tokenizer knows about.
   * @return currently {@code http}, {@code https}, and {@code ftp}
   * @since 2.1
   */
  public static List<String> getProtocols() {
    return PROTOCOLS;
  }

  /**
   * @since 3.0
   */
  public static boolean isUrl(String token) {
    for (String protocol : WordTokenizer.getProtocols()) {
      if (token.startsWith(protocol + "://") || token.startsWith("www.")) {
        return true;
      }
    }
    return false;
  }

  /**
   * @since 3.5
   */
  public static boolean isEMail(String token) {
    return E_MAIL.matcher(token).matches();
  }

  public WordTokenizer() {
  }

  @Override
  public List<String> tokenize(String text) {
    List<String> l = new ArrayList<>();
    StringTokenizer st = new StringTokenizer(text, getTokenizingCharacters(), true);
    while (st.hasMoreElements()) {
      l.add(st.nextToken());
    }
    return joinEMailsAndUrls(l);
  }

  /**
   * @return The string containing the characters used by the
   * tokenizer to tokenize words.
   * @since 2.5
   */
  public String getTokenizingCharacters() {
    return TOKENIZING_CHARACTERS;
  }

  protected List<String> joinEMailsAndUrls(List<String> list) {
    return joinUrls(joinEMails(list));
  }

  /**
   * @since 3.5
   */
  protected List<String> joinEMails(List<String> list) {
    StringBuilder sb = new StringBuilder();
    for (String item : list) {
      sb.append(item);
    }
    String text = sb.toString();
    if (E_MAIL.matcher(text).find()) {
      Matcher matcher = E_MAIL.matcher(text);
      List<String> l = new ArrayList<>();
      int currentPosition = 0, start, end, idx = 0;
      while (matcher.find()) {
        start = matcher.start();
        end = matcher.end();
        while (currentPosition < end) {
          if (currentPosition < start) {
            l.add(list.get(idx));
          } else if (currentPosition == start) {
            l.add(matcher.group());
          }
          currentPosition += list.get(idx).length();
          idx++;
        }
      }
      if (currentPosition < text.length()) {
        l.addAll(list.subList(idx, list.size()));
      }
      return l;
    }
    return list;
  }

  // see rfc1738 and http://stackoverflow.com/questions/1856785/characters-allowed-in-a-url
  protected List<String> joinUrls(List<String> l) {
    List<String> newList = new ArrayList<>();
    boolean inUrl = false;
    StringBuilder url = new StringBuilder();
    String urlQuote = null;
    for (int i = 0; i < l.size(); i++) {
      if (urlStartsAt(i, l)) {
        inUrl = true;
        if (i-1 >= 0) {
          urlQuote = l.get(i-1);
        }
        url.append(l.get(i));
      } else if (inUrl && urlEndsAt(i, l, urlQuote)) {
        inUrl = false;
        urlQuote = null;
        newList.add(url.toString());
        url.setLength(0);
        newList.add(l.get(i));
      } else if (inUrl) {
        url.append(l.get(i));
      } else {
        newList.add(l.get(i));
      }
    }
    if (url.length() > 0) {
      newList.add(url.toString());
    }
    return newList;
  }

  private boolean urlStartsAt(int i, List<String> l) {
    String token = l.get(i);
    if (isProtocol(token) && l.size() > i + 3) {
      String nToken = l.get(i + 1);
      String nnToken = l.get(i + 2);
      String nnnToken = l.get(i + 3);
      if (nToken.equals(":") && nnToken.equals("/") && nnnToken.equals("/")) {
        return true;
      }
    }
    if (l.size() > i + 1) {
      String nToken = l.get(i);
      String nnToken = l.get(i + 1);
      if (nToken.equals("www") && nnToken.equals(".")) {
        return true;
      }
    }
    return false;
  }

  private boolean isProtocol(String token) {
    return PROTOCOLS.contains(token);
  }

  private boolean urlEndsAt(int i, List<String> l, String urlQuote) {
    String token = l.get(i);
    if (StringTools.isWhitespace(token)) {
      return true;
    } else if (token.equals(")") || token.equals("]")) {   // this is guesswork
      return true;
    } else if (l.size() > i + 1) {
      String nToken = l.get(i + 1);
      if (StringTools.isWhitespace(nToken) &&
            (StringUtils.equalsAny(token, ".", ",", ";", ":", "!", "?") || token.equals(urlQuote))) {
        return true;
      }
    } else {
      Matcher matcher = URL_CHARS.matcher(token);
      if (!matcher.matches()) {
        return true;
      }
    }
    return false;
  }
  /** This function is designed to handle words like 12.3.a .   
   * 
   * @param tokens
   * @param input
   * @return
   */
  protected List<String> joinValidWord(List<String> tokens, String input) {
    
    int count = 0;
    Matcher matcher = ONE_WORD_TWO_DOT_PATTERN.matcher(input);
    int noOfMatch = 0;
    while (matcher.find())
      noOfMatch++;
 
    if(noOfMatch==0){
      return tokens;
    }
    matcher = ONE_WORD_TWO_DOT_PATTERN.matcher(input);
    
    while (matcher.find()) {
      boolean executed = false;
      String matchedStr = input.substring(matcher.start() + 1, matcher.end());
      count++;
      // Token Is Not At End
      if(matcher.start()!=0 && input.charAt(matcher.start())!= ' ' && input.charAt(matcher.start())!=','){
    	  continue;
      }
    	  
      if (matcher.end() != input.length()) {
        if (input.charAt(matcher.end()) == ',' || input.charAt(matcher.end()) == ' ') {

          if (matcher.start() == 0 && input.charAt(0)!=' ' && input.charAt(0)!=',') {
            matchedStr = input.substring(matcher.start(), matcher.end());
          }

          int index = getStartIndex(tokens, matchedStr);
          // Break The String
          if (index != -1) {

            List<String> tokenList = tokens.subList(index, index + 5);
            String newToken = String.join("", tokenList);
            List<String> frontList = tokens.subList(0, index);
            frontList.add(newToken);
            List<String> backList = tokens.subList(index + 6, tokens.size());
            frontList.addAll(backList);
            tokens = frontList;
            executed = true;
          }

        }
      }

      if (count == noOfMatch && executed == true)
        continue;
      
      // Last Token Case
      if (matcher.end() == input.length()) {
        List<String> combine = tokens.subList(tokens.size() - 5, tokens.size());
        tokens = tokens.subList(0, tokens.size() - 5);
        String str = String.join("", combine);
        tokens.add(str);
      } else if (matcher.end() == (input.length() - 1)) {
        List<String> combine = tokens.subList(tokens.size() - 6, tokens.size() - 1);
        String lastToken = tokens.get(tokens.size() - 1);
        tokens = tokens.subList(0, tokens.size() - 6);
        String str = String.join("", combine);
        tokens.add(str);
        tokens.add(lastToken);

      } else if (matcher.end() == (input.length() - 2)) {
        List<String> combine = tokens.subList(tokens.size() - 7, tokens.size() - 2);
        String lastToken = tokens.get(tokens.size() - 2);
        String lastToken1 = tokens.get(tokens.size() - 1);
        tokens = tokens.subList(0, tokens.size() - 7);
        String str = String.join("", combine);
        tokens.add(str);
        tokens.add(lastToken);
        tokens.add(lastToken1);
      }
    }
    return tokens;
  }

  /***
   *  This function returns the first index in from the list for a string 'token'
   * @param tokens
   * @param token
   * @return
   */
  static int getStartIndex(List<String> tokens, String token) {
    for (int i = 0; i < tokens.size() - 6; i++) {
      List<String> tokenList = tokens.subList(i, i + 5);
      if (String.join("", tokenList).equals(token)
          && (tokens.get(i + 5).equals(" ") || tokens.get(i + 5).equals(","))) {
        return i;
      }
    }
    return -1;
  }

}
