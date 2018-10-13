package com.github.mforoni.jcoder.demo;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.CodeGenerator;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jcoder.util.MoreSpreadsheets;
import com.github.mforoni.jspreadsheet.Spreadsheet;
import com.github.mforoni.jspreadsheet.Spreadsheets;

final class StatsGenerator {
  private static final Logger LOGGER = LoggerFactory.getLogger(StatsGenerator.class);
  static final String STATS_XLSX = "Stats_2015-16.xlsx";
  static final String SHEET_NAME = "Tutti";
  private static final String PACKAGE = "com.github.mforoni.jcoder.demo.generated";

  private StatsGenerator() {}

  static void start() throws IOException {
    LOGGER.info("Detecting header of file {}", STATS_XLSX);
    try (final Spreadsheet spreadsheet = Spreadsheets.open(JFiles.fromResource(STATS_XLSX))) {
      final JHeader musicHeader = MoreSpreadsheets.inferHeader(spreadsheet, SHEET_NAME);
      musicHeader.print(LOGGER);
      final CodeGenerator codeGenerator = new CodeGenerator("Stats", PACKAGE, musicHeader);
      codeGenerator.writeBean(JFiles.SRC_MAIN_JAVA);
      LOGGER.info("File Stats.java successfully written at {}", PACKAGE);
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
