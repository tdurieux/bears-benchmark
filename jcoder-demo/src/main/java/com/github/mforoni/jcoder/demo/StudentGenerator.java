package com.github.mforoni.jcoder.demo;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.CodeGenerator;
import com.github.mforoni.jcoder.JHeader;
import com.github.mforoni.jcoder.util.MoreCsv;

/**
 * 
 * @author Foroni Marco
 *
 */
final class StudentGenerator {
  static final String STUDENT_CSV = "student.csv";
  static final String PACKAGE = "com.github.mforoni.jcoder.demo.generated";
  private static final Logger LOGGER = LoggerFactory.getLogger(StudentGenerator.class);

  private StudentGenerator() {}

  static void start() throws IOException {
    LOGGER.info("Detecting header of file {}", STUDENT_CSV);
    final JHeader musicHeader = MoreCsv.inferHeader(STUDENT_CSV);
    musicHeader.print(LOGGER);
    final CodeGenerator codeGenerator = new CodeGenerator("Student", PACKAGE, musicHeader);
    codeGenerator.writeBean(JFiles.SRC_MAIN_JAVA);
    LOGGER.info("File Student.java successfully written at {}", PACKAGE);
  }

  public static void main(final String[] args) {
    try {
      start();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}
