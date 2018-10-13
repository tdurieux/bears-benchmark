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
@Beta
public final class Names {
  private Names() {
    throw new AssertionError();
  }

  /**
   * Returns the conventional field name from the given string i.e. the lower camel case version. If
   * the input contains spaces the string is trimmed and all consecutive spaces are replaced with
   * just one space. Then this normalized string is converted to lower camel case considering the
   * characters _ . - and space as word separators.
   * 
   * @param s the string to convert in a field name
   * @return the conventional field name from the given string.
   * @throws IllegalArgumentException if the given string is null or empty or does not contains at
   *         least one alphabetic character
   */
  @Nonnull
  public static String ofField(@Nonnull final String s) throws IllegalArgumentException {
    if (Strings.isNullOrEmpty(s)) {
      throw new IllegalArgumentException("The specified string must be not null and not empty");
    }
    final int indexOfFirstLetter = indexOfFirstLetter(s);
    if (indexOfFirstLetter == -1) {
      throw new IllegalArgumentException(
          "The specified string must contains at least one alphabetic character");
    }
    // removing non alphabetic initial characters
    final String ss = s.substring(indexOfFirstLetter);
    final String normalized =
        JStrings.contains(ss, ' ') ? JStrings.removeConsecutiveSpaces(ss.trim()) : ss;
    final String[] split = JStrings.split(normalized, ' ', '_', '.', '-');
    final List<String> parts = new ArrayList<>(split.length);
    parts.add(split[0].toLowerCase());
    for (int i = 1; i < split.length; i++) {
      if (split[i].length() > 0) {
        parts.add(JStrings.capitalize(split[i].toLowerCase()));
      }
    }
    return JStrings.concat(parts);
  }

  private static int indexOfFirstLetter(@Nonnull final String s) {
    for (int i = 0; i < s.length(); i++) {
      final char c = s.charAt(i);
      if (Character.isAlphabetic(c)) {
        return i;
      }
    }
    return -1;
  }

  @Nonnull
  public static String ofClass(@Nonnull final String s) {
    final String ofField = Names.ofField(s);
    return JStrings.capitalize(ofField.toLowerCase());
  }
}
