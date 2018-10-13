package com.github.mforoni.jcoder.demo;

import java.io.IOException;
import java.util.List;
import com.github.mforoni.jbasic.util.JLogger;
import com.github.mforoni.jcoder.demo.generated.Music;
import com.github.mforoni.jcoder.util.MoreCsv;

/**
 * 
 * @author Foroni Marco
 *
 */
final class MusicCsvReader {
  private MusicCsvReader() {}

  private static void start() throws IOException {
    final List<Music> musics =
        MoreCsv.parse(MusicGenerator.MUSIC_CSV, Music.class, Music.CELL_PROCESSOR);
    JLogger.info(musics, 10);
  }

  public static void main(final String[] args) {
    try {
      start();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}
