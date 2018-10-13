package com.github.mforoni.jcoder.demo;

import static com.github.mforoni.jcoder.demo.StudentGenerator.PACKAGE;
import java.io.IOException;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mforoni.jbasic.io.JFiles;
import com.github.mforoni.jcoder.JClass;
import com.squareup.javapoet.JavaFile;

final class PersonGenerator {
  private static final Logger LOGGER = LoggerFactory.getLogger(PersonGenerator.class);

  private PersonGenerator() {}

  public static void writePerson() throws IOException {
    final JClass jclass = new JClass.Builder("Person", PACKAGE). //
        field("firstName", String.class). //
        field("lastName", String.class). //
        field("dateBirth", LocalDate.class). //
        field("email", String.class).build();
    final JavaFile javaFile = jclass.buildImmutableClass();
    LOGGER.info("\n{}", javaFile);
    javaFile.writeTo(JFiles.SRC_MAIN_JAVA);
    LOGGER.info("Geneated file at {}", javaFile.packageName);
  }

  public static void main(final String[] args) {
    try {
      writePerson();
    } catch (final IOException ex) {
      ex.printStackTrace();
    }
  }
}
