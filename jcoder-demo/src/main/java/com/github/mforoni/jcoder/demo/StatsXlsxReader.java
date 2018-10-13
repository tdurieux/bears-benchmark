package com.github.mforoni.jcoder.demo;

import static com.github.mforoni.jcoder.demo.StatsGenerator.SHEET_NAME;
import static com.github.mforoni.jcoder.demo.StatsGenerator.STATS_XLSX;
import java.io.IOException;
import java.util.List;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jbasic.util.JLogger;
import com.github.mforoni.jcoder.demo.generated.Stats;
import com.github.mforoni.jcoder.util.MoreSpreadsheets;
import com.github.mforoni.jspreadsheet.Sheet;
import com.github.mforoni.jspreadsheet.Spreadsheet;
import com.github.mforoni.jspreadsheet.Spreadsheets;

final class StatsXlsxReader {
  private StatsXlsxReader() {}

  private static void start() throws IOException {
    try (final Spreadsheet spreadsheet = Spreadsheets.open(JFiles.fromResource(STATS_XLSX))) {
      final Sheet sheet = spreadsheet.getSheet(SHEET_NAME);
      final List<Stats> list = MoreSpreadsheets.readStrings(sheet, Stats.ROW_TO_STATS);
      JLogger.info(list, 10);
    }
  }

  public static void main(final String[] args) {
    try {
      start();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}
