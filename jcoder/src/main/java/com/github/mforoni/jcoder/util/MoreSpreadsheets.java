package com.github.mforoni.jcoder.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.github.mforoni.jcoder.JClass;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jspreadsheet.Sheet;
import com.github.mforoni.jspreadsheet.Spreadsheet;
import com.google.common.annotations.Beta;
import com.google.common.base.Function;

/**
 * Provides {@code static} utility methods to read data from spreadsheets into Java Collection
 * objects. It also provided API for retrieving {@link JHeader} objects from spreadsheets that can
 * be used for automatically generate JavaBean or immutable java class using {@link JClass}
 * functionalities.
 * 
 * @author Foroni Marco
 * @see JHeader
 * @see JClass
 * @see Spreadsheet
 * @see Sheet
 */
@Beta
public final class MoreSpreadsheets {
  private MoreSpreadsheets() {
    throw new AssertionError();
  }

  public static JHeader inferHeader(final Spreadsheet spreadsheet, final String sheetName) {
    final Sheet sheet = spreadsheet.getSheet(sheetName);
    return inferHeader(spreadsheet, sheet, sheet.getColumns(), true);
  }

  private static JHeader inferHeader(final Spreadsheet spreadsheet, final Sheet sheet,
      final int columns, final boolean header) {
    final Parser.Strings parser = new Parser.Strings(spreadsheet.getFile().getName());
    final int rows = sheet.getRows();
    for (int row = 0; row < rows; row++) {
      final String[] values = new String[columns];
      for (int col = 0; col < columns; col++) {
        values[col] = sheet.getRawValue(row, col);
      }
      if (header && row == 0) {
        parser.header(values);
      } else {
        parser.values(values, row);
      }
    }
    return parser.buildHeader();
  }

  public static JHeader buildHeader(final Spreadsheet spreadsheet, final String sheetName) {
    final Sheet sheet = spreadsheet.getSheet(sheetName);
    return buildHeader(spreadsheet, sheet, sheet.getColumns(), true);
  }

  private static JHeader buildHeader(final Spreadsheet spreadsheet, final Sheet sheet,
      final int columns, final boolean header) {
    final Parser.Objects parser = new Parser.Objects(spreadsheet.getFile().getName());
    final int rows = sheet.getRows();
    final int cols = columns != -1 ? columns : sheet.getColumns();
    for (int row = 0; row < rows; row++) {
      final Object[] values = sheet.getRow(row, 0, cols);
      if (header && row == 0) {
        parser.header(values);
      } else {
        parser.values(values, row);
      }
    }
    return parser.buildHeader();
  }

  public static <T> List<T> read(final Sheet sheet, final Function<Object[], T> function) {
    return read(sheet, 1, sheet.getRows() - 1, function);
  }

  public static <T> List<T> read(final Sheet sheet, final int fromRowIndex, final int toRowIndex,
      final Function<Object[], T> function) {
    final List<T> list = new ArrayList<>();
    for (int r = fromRowIndex; r <= toRowIndex; r++) {
      final Object[] values = sheet.getRow(r);
      final T obj = function.apply(values);
      list.add(obj);
    }
    return list;
  }

  public static <T> List<T> readStrings(final Sheet sheet, final Function<String[], T> function) {
    return readStrings(sheet, 1, sheet.getRows() - 1, function);
  }

  public static <T> List<T> readStrings(final Sheet sheet, final int fromRowIndex,
      final int toRowIndex, final Function<String[], T> function) {
    final List<T> list = new ArrayList<>();
    final int columns = sheet.getColumns();;
    for (int r = fromRowIndex; r <= toRowIndex; r++) {
      final String[] values = new String[columns];
      for (int c = 0; c < columns; c++) {
        final String rawValue = sheet.getRawValue(r, c);
        values[c] = rawValue.isEmpty() ? null : rawValue;
      }
      try {
        final T obj = function.apply(values);
        list.add(obj);
      } catch (final Throwable t) {
        throw new IllegalStateException(String.format("Error while parsing sheet %s, row %d: %s",
            sheet.getName(), r, Arrays.asList(values)), t);
      }
    }
    return list;
  }
}
