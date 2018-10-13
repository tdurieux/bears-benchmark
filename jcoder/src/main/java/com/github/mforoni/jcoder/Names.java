package com.github.mforoni.jcoder;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import com.github.mforoni.jbasic.JStrings;
import com.google.common.annotations.Beta;
import com.google.common.base.Strings;

/**
 * Provides naming conventions
 * 
 * @author Foroni
 */
public final class Names {
  /**
   * Returns the conventional field name from the given string i.e. the lower camel case version. If
   * the input contains spaces the string is trimmed and all consecutive spaces are replaced with
   * just one space. Then this normalized string is converted to lower case according to the
   * following:
   * <ul>
   * <li>if the normalized string is alphabetic and all in upper case returns the string in lower
   * case</li>
   * <li>if the normalized string is alphabetic and the first character is in upper case return the
   * string decapitalized</li>
   * <li>if the normalized string contains {@code ' '} or {@code '_'} then conversion to lower camel
   * case is performed according to converted to
   * {@link JStrings#lowerCamelCase(String, char, char...)}</li>
   * <li>if no previous conditions are satisfied throws an {@code IllegalArgumentException}.</li>
   * </ul>
   * 
   * @param s the string to convert in a field name
   * @return the conventional field name from the given string.
   * @throws IllegalArgumentException if the given string is null or empty
   */
  @Beta
  @Nonnull
  public static String ofField(@Nonnull final String s) throws IllegalArgumentException {
    if (Strings.isNullOrEmpty(s)) {
      throw new IllegalArgumentException("The specified string must be not null and not empty");
    }
    final String normalized =
        JStrings.contains(s, ' ') ? JStrings.removeConsecutiveSpaces(s.trim()) : s;
    final String[] split = JStrings.split(normalized, ' ', '_', '.', '-');
    final List<String> parts = new ArrayList<>(split.length);
    final String start =
        split.length > 1 ? split[0].toLowerCase() : JStrings.decapitalize(split[0]);
    // FIXME if start.chartAt(0) is not letter
    parts.add(start);
    for (int i = 1; i < split.length; i++) {
      final String word = split[i];
      parts.add(JStrings.capitalize(word.toLowerCase()));
    }
    return JStrings.concat(parts);
  }
}
