package com.github.mforoni.jcoder.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.github.mforoni.jbasic.JBooleans;
import com.github.mforoni.jbasic.time.JLocalDates;
import com.github.mforoni.jbasic.time.JLocalDates.InferredLocalDate;

/**
 * 
 * @author Foroni Marco
 *
 */
class InferredType {
  static final InferredType INTEGER_BOOLEAN = new InferredType(Boolean.class);
  static final InferredType STRING_BOOLEAN = new InferredType(Boolean.class);
  static final InferredType INTEGER = new InferredType(Integer.class);
  static final InferredType DOUBLE = new InferredType(Double.class);
  static final InferredType STRING = new InferredType(String.class);
  private final Class<?> type;
  private final List<String> formats;

  InferredType(@Nonnull final Class<?> type, @Nullable final List<String> formats) {
    super();
    this.type = type;
    this.formats = formats;
  }

  public InferredType(@Nonnull final Class<?> type) {
    this(type, null);
  }

  public Class<?> getType() {
    return type;
  }

  @Nullable
  public List<String> getFormats() {
    return formats;
  }

  @Override
  public String toString() {
    return "InferredType [type=" + type + ", formats=" + formats + "]";
  }

  public static InferredType inferCommonType(@Nullable final InferredType first,
      @Nullable final InferredType second) {
    if (first == null) {
      return second;
    } else if (second != null) {
      return _inferCommonType(first, second);
    } else {
      return first;
    }
  }

  @Nonnull
  public static InferredType _inferCommonType(@Nonnull final InferredType first,
      @Nonnull final InferredType second) {
    final Class<?> type1 = first.getType();
    final Class<?> type2 = second.getType();
    if (type1.equals(String.class) || type2.equals(String.class)) {
      return STRING;
    } else if (type1.equals(Date.class)) {
      if (type2.equals(Date.class)) {
        first.getFormats().retainAll(second.getFormats());
        return first;
      } else if (type2.equals(Boolean.class)) {
        return STRING;
      } else if (type2.equals(Integer.class)) {
        return STRING;
      } else if (type2.equals(Double.class)) {
        return STRING;
      }
    } else if (type1.equals(Boolean.class)) {
      if (type2.equals(Date.class)) {
        return STRING;
      } else if (type2.equals(Boolean.class)) {
        return first == STRING_BOOLEAN || second == STRING_BOOLEAN ? STRING_BOOLEAN : first;
      } else if (type2.equals(Integer.class)) {
        return first == INTEGER_BOOLEAN ? INTEGER : STRING;
      } else if (type2.equals(Double.class)) {
        return first == INTEGER_BOOLEAN ? DOUBLE : STRING;
      }
    } else if (type1.equals(Integer.class)) {
      if (type2.equals(Date.class)) {
        return STRING;
      } else if (type2.equals(Boolean.class)) {
        return second == INTEGER_BOOLEAN ? INTEGER : STRING;
      } else if (type2.equals(Integer.class)) {
        return INTEGER;
      } else if (type2.equals(Double.class)) {
        return DOUBLE;
      }
    } else if (type1.equals(Double.class)) {
      if (type2.equals(Date.class)) {
        return STRING;
      } else if (type2.equals(Boolean.class)) {
        return second == INTEGER_BOOLEAN ? DOUBLE : STRING;
      } else if (type2.equals(Integer.class)) {
        return DOUBLE;
      } else if (type2.equals(Double.class)) {
        return DOUBLE;
      }
    }
    throw new IllegalStateException("Case not handled: type1=" + type1 + ", type2=" + type2);
  }

  @Nullable
  public static InferredType inferType(@Nullable final String value) {
    if (value == null || value.length() == 0) {
      return null;
    } else if (value.length() == 1 && (value.charAt(0) == '0' || value.charAt(0) == '1')) {
      return INTEGER_BOOLEAN;
    } else {
      try {
        JBooleans.parse(value);
        return STRING_BOOLEAN;
      } catch (final IllegalArgumentException e) {
        // ignored
      }
      final List<InferredLocalDate> inferredDates = JLocalDates.inferredLocalDates(value);
      if (inferredDates.size() > 0) {
        final List<String> formats = new ArrayList<>();
        for (final InferredLocalDate i : inferredDates) {
          formats.add(i.getFormat());
        }
        return new InferredType(Date.class, formats);
      } else {
        try {
          Integer.parseInt(value);
          return INTEGER;
        } catch (final NumberFormatException e) {
          // Ignored
        }
        try {
          Double.parseDouble(value);
          return DOUBLE;
        } catch (final NumberFormatException e) {
          // Ignored
        }
        return STRING;
      }
    }
  }
}
