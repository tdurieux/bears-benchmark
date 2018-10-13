package com.github.mforoni.jcoder;

import java.util.Date;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import javax.lang.model.element.Modifier;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseDate;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import com.github.mforoni.jbasic.JStrings;
import com.github.mforoni.jbasic.reflect.JMethods;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;

/**
 * @author Foroni Marco
 */
@Immutable
public final class JField {
  public static final Function<JField, MethodSpec> TO_GETTER_METHODSPEC =
      new Function<JField, MethodSpec>() {
        @Override
        @Nullable
        public MethodSpec apply(@Nullable final JField input) {
          return input != null ? input.toGetter() : null;
        }
      };
  public static final Function<JField, CodeBlock> TO_CELL_PROCESSOR =
      new Function<JField, CodeBlock>() {
        @Override
        @Nullable
        public CodeBlock apply(@Nullable final JField input) {
          return input != null ? toCellProcessor(input) : null;
        }
      };
  public static final Function<JField, FieldSpec> TO_PRIVATE_FINAL_FIELDSPEC =
      new Function<JField, FieldSpec>() {
        @Override
        @Nullable
        public FieldSpec apply(@Nullable final JField input) {
          return input != null ? FieldSpec
              .builder(input.getType(), input.getName(), Modifier.PRIVATE, Modifier.FINAL).build()
              : null;
        }
      };
  public static final Function<JField, FieldSpec> TO_PRIVATE_FIELDSPEC =
      new Function<JField, FieldSpec>() {
        @Override
        @Nullable
        public FieldSpec apply(@Nullable final JField input) {
          return input != null
              ? FieldSpec.builder(input.getType(), input.getName(), Modifier.PRIVATE).build()
              : null;
        }
      };
  public static final Function<JField, Class<?>> TO_TYPE = new Function<JField, Class<?>>() {
    @Override
    @Nullable
    public Class<?> apply(@Nullable final JField input) {
      return input == null ? null : input.getType();
    }
  };
  public static final Function<JField, String> TO_NAME = new Function<JField, String>() {
    @Override
    @Nullable
    public String apply(@Nullable final JField input) {
      return input == null ? null : input.getName();
    }
  };
  public static final Function<JField, String> TO_CONVENTIONAL_FIELD_NAME =
      new Function<JField, String>() {
        @Override
        public String apply(@Nullable final JField input) {
          return input == null ? null : conventionalFieldName(input.getName());
        }
      };
  public static final Function<JField, String> TO_CONVENTIONAL_CONSTANT_NAME =
      new Function<JField, String>() {
        @Override
        public String apply(@Nullable final JField input) {
          return input == null ? null : conventionalFieldName(input.getName()).toUpperCase();
        }
      };

  public static String getterMethodName(final JField field) {
    return JMethods.getterName(field.getName(), field.getType());
  }

  public static FieldSpec constantStringField(final String name) {
    return FieldSpec.builder(String.class, conventionalFieldName(name).toUpperCase(),
        JClass.PUBLIC_STATIC_FINAL).initializer("\"$N\"", name).build();
  }

  public static String conventionalFieldName(final String name) {
    return JStrings.replaceNotAlphanumeric(name, "_"); // FIXME improve
  }

  public static CodeBlock toCellProcessor(final JField field) {
    final Class<?> firstType =
        field.isNullable() ? org.supercsv.cellprocessor.Optional.class : NotNull.class;
    final Class<?> secondType;
    if (field.getType().equals(String.class)) {
      secondType = null;
    } else if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
      secondType = ParseInt.class;
    } else if (field.getType().equals(Long.class) || field.getType().equals(Long.TYPE)) {
      secondType = ParseLong.class;
    } else if (field.getType().equals(Double.class) || field.getType().equals(Double.TYPE)) {
      secondType = ParseDouble.class;
    } else if (field.getType().equals(Boolean.class) || field.getType().equals(Boolean.TYPE)) {
      secondType = ParseBool.class;
    } else if (field.getType().equals(Date.class)) {
      return CodeBlock.builder()
          .add("new $T(new $T(\"$N\"))", firstType, ParseDate.class, field.getFormat()).build();
    } else {
      throw new IllegalStateException("Case not handled for type " + field.getType());
    }
    return secondType == null ? CodeBlock.builder().add("new $T()", firstType).build()
        : CodeBlock.builder().add("new $T(new $T())", firstType, secondType).build();
  }

  @Nonnull
  private final String name;
  @Nonnull
  private final Class<?> type;
  private final boolean nullable;
  private final String format;

  public JField(@Nonnull final String name, @Nonnull final Class<?> type, final boolean nullable) {
    this(name, type, nullable, null);
  }

  public JField(@Nonnull final String name, @Nonnull final Class<?> type, final boolean nullable,
      @Nullable final String format) {
    super();
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(type);
    if (type == void.class) {
      throw new IllegalArgumentException("void is not a valid type for a field");
    }
    if (type.isPrimitive() && nullable) {
      throw new IllegalArgumentException("A primitive type cannot be nullable");
    }
    this.name = name;
    this.type = type;
    this.nullable = nullable;
    this.format = format;
  }

  @Nonnull
  public String getName() {
    return name;
  }

  @Nonnull
  public Class<?> getType() {
    return type;
  }

  public boolean isNullable() {
    return nullable;
  }

  public String getFormat() {
    return format;
  }

  public MethodSpec toGetter() {
    return MethodSpecs.getterMethodSpec(this);
  }

  public MethodSpec toSetter() {
    return MethodSpecs.setterMethodSpec(this);
  }

  @Override
  public String toString() {
    return "JField [name=" + name + ", type=" + type + ", nullable=" + nullable + "]";
  }

  public static class Builder {
    private final String name;
    private Class<?> type;
    private boolean nullable;

    public Builder(final String name) {
      this.name = name;
      this.type = null;
      this.nullable = false;
    }

    public Builder type(final Class<?> type) {
      this.type = type;
      return this;
    }

    public Builder nullable(final boolean nullable) {
      this.nullable = nullable;
      return this;
    }

    public JField build() {
      return new JField(name, type, nullable);
    }
  }
}
