package com.github.mforoni.jcoder.demo;

import static com.github.mforoni.jcoder.demo.StudentGenerator.PACKAGE;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.CodeGenerator;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jcoder.util.MoreSpreadsheets;
import com.github.mforoni.jspreadsheet.Spreadsheet;
import com.github.mforoni.jspreadsheet.Spreadsheets;

final class ArtistGenerator {
  static final String ARTISTS_ODS = "artists.ods";
  static final String SHEET_NAME = "music";
  private static final Logger LOGGER = LoggerFactory.getLogger(ArtistGenerator.class);

  private ArtistGenerator() {}

  static void start() throws IOException {
    LOGGER.info("Detecting header of file {}", ARTISTS_ODS);
    try (final Spreadsheet spreadsheet = Spreadsheets.open(JFiles.fromResource(ARTISTS_ODS))) {
      final JHeader musicHeader = MoreSpreadsheets.inferHeader(spreadsheet, SHEET_NAME);
      musicHeader.print(LOGGER);
      final CodeGenerator codeGenerator = new CodeGenerator("Artist", PACKAGE, musicHeader);
      codeGenerator.writeBean(JFiles.SRC_MAIN_JAVA);
      LOGGER.info("File Artist.java successfully written at {}", PACKAGE);
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
