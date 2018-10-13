package com.github.mforoni.jcoder;

import java.io.IOException;
import com.squareup.javapoet.JavaFile;

public final class JavaFiles {
  private JavaFiles() {
    throw new AssertionError();
  }

  public static String toString(final JavaFile javaFile) {
    final StringBuilder sb = new StringBuilder();
    try {
      javaFile.writeTo(sb);
    } catch (final IOException ex) {
      throw new IllegalStateException(ex);
    }
    return sb.toString();
  }
}
