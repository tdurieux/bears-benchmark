package com.github.mforoni.jcoder.demo;

import static com.github.mforoni.jcoder.demo.StudentGenerator.PACKAGE;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.CodeGenerator;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jcoder.util.CsvReader;
import com.github.mforoni.jcoder.util.MoreCsv;

final class MusicGenerator {
  static final String MUSIC_CSV = "music.csv";
  private static final Logger LOGGER = LoggerFactory.getLogger(MusicGenerator.class);

  private MusicGenerator() {}

  static void start() throws IOException {
    LOGGER.info("Detecting header of file {}", MUSIC_CSV);
    final JHeader musicHeader = MoreCsv.inferHeader(new CsvReader.Builder(MUSIC_CSV).build());
    musicHeader.print(LOGGER);
    final CodeGenerator codeGenerator = new CodeGenerator("Music", PACKAGE, musicHeader);
    codeGenerator.writeBean(JFiles.SRC_MAIN_JAVA);
    LOGGER.info("File Music.java successfully written at {}", PACKAGE);
  }

  public static void main(final String[] args) {
    try {
      start();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}
