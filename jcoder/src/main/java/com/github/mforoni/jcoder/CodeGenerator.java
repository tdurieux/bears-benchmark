package com.github.mforoni.jcoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import javax.annotation.concurrent.Immutable;
import com.squareup.javapoet.JavaFile;

/**
 * @author Foroni Marco
 */
@Immutable
public final class CodeGenerator {
  private final String className;
  private final String packageName;
  private final JHeader header;

  public CodeGenerator(final String className, final String packageName, final JHeader header) {
    super();
    this.className = className;
    this.packageName = packageName;
    this.header = header;
  }

  public JavaFile generateImmutableClass() {
    final JClass jclass = new JClass(className, packageName, header.getFields());
    return jclass.buildImmutableClass(header.getOrigin());
  }

  public void writeImmutableClass(final String path) throws IOException {
    generateImmutableClass().writeTo(new File(path));
  }

  public JavaFile generateBean() {
    final JClass jclass = new JClass(className, packageName, header.getFields());
    return jclass.buildBean(header.getOrigin());
  }

  public void writeBean(final String path) throws IOException {
    generateBean().writeTo(new File(path));
  }

  public void writeBean(final Path path) throws IOException {
    generateBean().writeTo(path.toFile());
  }

  public JavaFile generateHeaderClass() {
    final JClass jclass = new JClass(className, packageName, header.getFields());
    return jclass.buildHeaderClass(header.getOrigin());
  }

  public void writeHeaderClass(final Path path) throws IOException {
    generateHeaderClass().writeTo(path.toFile());
  }

  public void writeHeaderClass(final String pathname) throws IOException {
    generateHeaderClass().writeTo(new File(pathname));
  }

  @Override
  public String toString() {
    return "CodeGenerator [className=" + className + ", packageName=" + packageName + ", header="
        + header + "]";
  }
}
