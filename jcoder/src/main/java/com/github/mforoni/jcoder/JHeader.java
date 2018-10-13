package com.github.mforoni.jcoder;

import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import com.github.mforoni.jbasic.JStrings;
import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.squareup.javapoet.TypeName;

/**
 * Represents a generic header, i.e. a list of fields, typically retrieved from a CSV file or a
 * spreadsheet.
 * <p>
 * Using the API provided by {@link JClass} with this list of fields it is possible to automatically
 * write java code representing objects like JavaBeans and Immutable classes.
 * 
 * @author Foroni Marco
 * @see JField
 * @see JClass
 */
public final class JHeader {
  private final List<JField> fields;
  private final Optional<Origin> origin;

  public JHeader(final List<JField> fields, final Optional<Origin> origin) {
    this.fields = fields;
    this.origin = origin;
  }

  public JHeader(final List<JField> fields) {
    this(fields, Optional.<Origin>absent());
  }

  public List<JField> getFields() {
    return fields;
  }

  public Optional<Origin> getOrigin() {
    return origin;
  }

  public void print(final Logger logger) {
    for (int i = 0; i < fields.size(); i++) {
      final JField field = fields.get(i);
      logger.info("Index {}: {}", i, field);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("fields", fields).add("origin", origin).toString();
  }

  public enum ParserType {
    STRINGS, OBJECTS;
    public TypeName toTypeName() {
      return toTypeName(this);
    }

    public Class<?> toType() {
      return toType(this);
    }

    public Class<?> toArrayType() {
      return toArrayType(this);
    }

    private static TypeName toTypeName(final ParserType parserType) {
      switch (parserType) {
        case OBJECTS:
          return TypeName.get(Object[].class);
        case STRINGS:
          return TypeName.get(String[].class);
        default:
          throw new AssertionError();
      }
    }

    private static Class<?> toType(final ParserType parserType) {
      switch (parserType) {
        case OBJECTS:
          return Object.class;
        case STRINGS:
          return String.class;
        default:
          throw new AssertionError();
      }
    }

    private static Class<?> toArrayType(final ParserType parserType) {
      switch (parserType) {
        case OBJECTS:
          return Object[].class;
        case STRINGS:
          return String[].class;
        default:
          throw new AssertionError();
      }
    }
  }
  @Beta
  public static class Origin {
    @Nonnull
    private final String filepath;
    @Nullable
    private final ParserType parserType;

    public Origin(final String filepath, final ParserType parserType) {
      this.filepath = filepath;
      this.parserType = parserType;
    }

    public Origin(final String filepath) {
      this(filepath, null);
    }

    public String getFilepath() {
      return filepath;
    }

    public ParserType getParserType() {
      return parserType;
    }

    public Type getType() {
      final String extension = Files.getFileExtension(filepath);
      if (extension.equalsIgnoreCase("csv")) {
        return Type.CSV;
      } else if (JStrings.matchIgnoreCase(extension, "xlsx", "xls", "ods")) {
        return Type.SPREADSHEET;
      } else {
        throw new IllegalStateException("Cannot retrieve Origin.Type from filepath: " + filepath);
      }
    }

    public enum Type {
      CSV, SPREADSHEET
    }

    @Override
    public String toString() {
      return "Origin [filepath=" + filepath + ", parserType=" + parserType + "]";
    }
  }
}
