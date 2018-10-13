package com.github.mforoni.jcoder.util;

import java.io.File;
import java.io.FileNotFoundException;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.Immutable;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.JHeader;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.io.Files;

/**
 * Contains information for reading a CSV file or a pseudo CSV file where the separator character is
 * not the standard comma:
 * <ul>
 * <li>the CSV file</li>
 * <li>the separator character used in the CSV file</li>
 * <li>the header presence as a boolean value</li>
 * <li>the row limit, i.e. the maximum number of rows to read</li>
 * </ul>
 * 
 * @author Foroni Marco
 * @see JHeader
 */
@Immutable
public class CsvReader {
  private final static int NO_ROW_LIMIT = Integer.MAX_VALUE;
  public static final char DEFAULT_SEPARATOR = ',';
  private final File csv;
  private final char separator;
  private final int rowLimit;
  private final boolean header;

  public CsvReader(@Nonnull final File csv, final char separator, final boolean header,
      final int rowLimit) {
    super();
    Preconditions.checkArgument(Files.getFileExtension(csv.getName()).equalsIgnoreCase("csv"));
    this.csv = csv;
    this.separator = separator;
    this.header = header;
    this.rowLimit = rowLimit;
  }

  public CsvReader(@Nonnull final File csv) {
    this(csv, DEFAULT_SEPARATOR, true, NO_ROW_LIMIT);
  }

  @Nonnull
  public File getCsv() {
    return csv;
  }

  public char getSeparator() {
    return separator;
  }

  public boolean isHeader() {
    return header;
  }

  public int getRowLimit() {
    return rowLimit;
  }

  public static class Builder {
    private final File csv;
    private char separator;
    private boolean header;
    private int rowLimit;

    public Builder(final File csv) {
      this.csv = csv;
      this.separator = DEFAULT_SEPARATOR;
      this.header = true;
      this.rowLimit = NO_ROW_LIMIT;
    }

    public Builder(final String resource) throws FileNotFoundException {
      this(JFiles.fromResource(resource));
    }

    public Builder separator(final char separator) {
      this.separator = separator;
      return this;
    }

    public Builder header(final boolean value) {
      this.header = value;
      return this;
    }

    public Builder rowLimit(final int rowLimit) {
      this.rowLimit = rowLimit;
      return this;
    }

    public CsvReader build() {
      return new CsvReader(csv, separator, header, rowLimit);
    }
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("csv", csv).add("separator", separator)
        .add("header", header).add("rowLimit", rowLimit).toString();
  }
}
