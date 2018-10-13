package com.github.mforoni.jcoder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.github.mforoni.jcoder.JField;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jcoder.JHeader.Origin;
import com.github.mforoni.jcoder.JHeader.ParserType;
import com.github.mforoni.jcoder.Names;
import com.google.common.base.Optional;

final class Parser {
  private Parser() {
    throw new AssertionError();
  }

  static class Objects {
    private final List<MutableField> fields = new ArrayList<>();
    private final String filepath;

    Objects(final String filepath) {
      this.filepath = filepath;
    }

    void header(final Object[] header) {
      if (fields.size() > 0) {
        throw new IllegalStateException();
      }
      int counter = 1;
      for (final Object h : header) {
        fields.add(new MutableField(toFieldName(h == null ? null : h.toString(), counter++)));
      }
    }

    private void header(final int size) {
      for (int i = 1; i <= size; i++) {
        fields.add(new MutableField(toFieldName(null, i)));
      }
    }

    public void values(@Nonnull final Object[] values, final int row) {
      final int headerSize = fields.size();
      if (headerSize == 0) {
        header(headerSize);
      }
      if (values.length > headerSize) {
        throw new IllegalStateException(String.format(
            "Number of values (%d) not consistent with header in first line (%d) at line %d\nrow=%s",
            values.length, headerSize, row, Arrays.asList(values)));
      }
      try {
        for (int i = 0; i < values.length; i++) {
          final MutableField field = fields.get(i);
          if (values[i] != null) {
            final Class<?> type = values[i].getClass();
            field.setType(type);
          } else {
            field.setNullable(true);
          }
        }
      } catch (final Throwable t) {
        throw new IllegalStateException(
            "Something unexpected at row " + row + ": " + Arrays.asList(values), t);
      }
    }

    JHeader buildHeader() {
      if (this.fields.size() == 0) {
        throw new IllegalStateException();
      }
      final List<JField> fields = new ArrayList<>();
      for (final MutableField mf : this.fields) {
        fields.add(mf.toJField());
      }
      return new JHeader(fields, Optional.of(new Origin(filepath, ParserType.OBJECTS)));
    }
  }
  static class Strings {
    private final List<MutableInferredField> fields = new ArrayList<>();
    private final String filepath;

    Strings(final String filepath) {
      this.filepath = filepath;
    }

    void header(@Nonnull final String[] header) {
      if (fields.size() > 0) {
        throw new IllegalStateException();
      }
      int counter = 1;
      for (final String h : header) {
        fields.add(new MutableInferredField(toFieldName(h, counter++)));
      }
    }

    private void header(final int size) {
      for (int i = 1; i <= size; i++) {
        fields.add(new MutableInferredField(toFieldName(null, i)));
      }
    }

    void values(@Nonnull final String[] values, final int row) {
      final int headerSize = fields.size();
      if (headerSize == 0) {
        header(headerSize);
      }
      if (values.length != headerSize) {
        throw new IllegalStateException(String.format(
            "Number of values (%d) not consistent with header in first line (%d) at line %d\nrow=%s",
            values.length, headerSize, row, Arrays.asList(values)));
      }
      try {
        for (int i = 0; i < values.length; i++) {
          final MutableInferredField field = fields.get(i);
          if (!com.google.common.base.Strings.isNullOrEmpty(values[i])) {
            final InferredType newType = InferredType.inferType(values[i]);
            final InferredType type = field.getInferredType();
            field.setInferredType(InferredType.inferCommonType(type, newType));
          } else {
            field.setNullable(true);
          }
        }
      } catch (final Throwable t) {
        throw new IllegalStateException(
            "Something unexpected at row " + row + ": " + Arrays.asList(values), t);
      }
    }

    public JHeader buildHeader() {
      if (this.fields.size() == 0) {
        throw new IllegalStateException();
      }
      final List<JField> fields = new ArrayList<>();
      for (final MutableInferredField field : this.fields) {
        fields.add(field.toJField());
      }
      return new JHeader(fields, Optional.of(new Origin(filepath, ParserType.STRINGS)));
    }
  }

  @Nonnull
  private static String toFieldName(@Nullable final String s, final int counter) {
    return com.google.common.base.Strings.isNullOrEmpty(s) ? "field" + counter : Names.ofField(s);
  }
}
