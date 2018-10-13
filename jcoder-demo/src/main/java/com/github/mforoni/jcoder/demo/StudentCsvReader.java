package com.github.mforoni.jcoder.demo;

import java.io.IOException;
import java.util.List;
import com.github.mforoni.jbasic.util.JLogger;
import com.github.mforoni.jcoder.demo.generated.Student;
import com.github.mforoni.jcoder.util.MoreCsv;

public class StudentCsvReader {
  private StudentCsvReader() {}

  private static void start() throws IOException {
    final List<Student> students =
        MoreCsv.parse(StudentGenerator.STUDENT_CSV, Student.class, Student.CELL_PROCESSOR);
    JLogger.info(students, 10);
  }

  public static void main(final String[] args) {
    try {
      start();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}
