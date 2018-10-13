package com.github.mforoni.jcoder;

import com.github.mforoni.jbasic.JStrings;
import com.google.common.annotations.Beta;
import com.squareup.javapoet.CodeBlock;

@Beta
public final class ControlFlow implements JStatement {
  private final String s;

  private ControlFlow(final Builder builder) {
    this.s = builder.build();
  }

  @Override
  public String toString() {
    return s;
  }

  @Override
  public CodeBlock toCodeBlock() {
    return CodeBlock.of(s);
  }

  public static Switch switchBuilder(final String varName) {
    return new Switch(varName);
  }

  private interface Builder {
    String build();
  }
  public static class For implements Builder {
    private final StringBuilder sb;

    public For(final String varName, final String to) {
      this(varName, "0", to);
    }

    public For(final String varName, final String start, final String to) {
      sb = new StringBuilder(String.format("for (int %s = %s; %s < %s; %s++) {%s", varName, start,
          varName, to, varName, JStrings.NEWLINE));
    }

    public For statement(final JStatement statement) {
      return statement(statement.toString());
    }

    public For statement(final String format, final Object... args) {
      if (args != null && args.length > 0) {
        sb.append(String.format(format, args));
      } else {
        sb.append(format);
      }
      sb.append(';').append(JStrings.NEWLINE);
      return this;
    }

    @Override
    public String build() {
      return sb.append('}').append(JStrings.NEWLINE).toString();
    }
  }
  public static class If implements Builder {
    private final StringBuilder sb;

    public If(final String condition) {
      sb = new StringBuilder("if (").append(condition).append(") {").append(JStrings.NEWLINE);
    }

    public If statement(final JStatement statement) {
      return statement(statement.toString());
    }

    public If statement(final String format, final Object... args) {
      if (args != null && args.length > 0) {
        sb.append(String.format(format, args));
      } else {
        sb.append(format);
      }
      sb.append(';').append(JStrings.NEWLINE);
      return this;
    }

    @Override
    public String build() {
      return sb.append('}').append(JStrings.NEWLINE).toString();
    }
  }
  public static class Switch implements Builder {
    private final StringBuilder sb;

    public Switch(final String varName) {
      sb = new StringBuilder(String.format("switch (%s) {%s", varName, JStrings.NEWLINE));
    }

    public Switch addCase(final int value, final JStatement statement) {
      return addCase(value, statement.toString());
    }

    public Switch addCase(final int value, final String statement, final Object... args) {
      final String strStatement =
          (args != null && args.length > 0) ? String.format(statement, args) : statement;
      sb.append(String.format("case %d:%s", value, JStrings.NEWLINE)).append(strStatement)
          .append(';').append(JStrings.NEWLINE);
      if (!strStatement.contains("return")) {
        sb.append("break;").append(JStrings.NEWLINE);
      }
      return this;
    }

    @Override
    public String build() {
      return sb.append('}').append(JStrings.NEWLINE).toString();
    }
  }
}
