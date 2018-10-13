package com.github.mforoni.jcoder.demo;

import static com.github.mforoni.jcoder.demo.ArtistGenerator.ARTISTS_ODS;
import static com.github.mforoni.jcoder.demo.ArtistGenerator.SHEET_NAME;
import java.io.IOException;
import java.util.List;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jbasic.util.JLogger;
import com.github.mforoni.jcoder.demo.generated.Artist;
import com.github.mforoni.jcoder.util.MoreSpreadsheets;
import com.github.mforoni.jspreadsheet.Sheet;
import com.github.mforoni.jspreadsheet.Spreadsheet;
import com.github.mforoni.jspreadsheet.Spreadsheets;

final class ArtistsOdsReader {
  private ArtistsOdsReader() {}

  private static void start() throws IOException {
    try (final Spreadsheet spreadsheet = Spreadsheets.open(JFiles.fromResource(ARTISTS_ODS))) {
      final Sheet sheet = spreadsheet.getSheet(SHEET_NAME);
      final List<Artist> artists = MoreSpreadsheets.readStrings(sheet, Artist.ROW_TO_ARTIST);
      JLogger.info(artists, 10);
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
