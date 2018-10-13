package com.github.mforoni.jcoder;

import com.google.common.annotations.Beta;
import com.squareup.javapoet.CodeBlock;

/**
 * @author Foroni Marco
 */
@Beta
public interface JStatement {
  @Override
  public String toString();

  public CodeBlock toCodeBlock();
}
