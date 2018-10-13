package com.github.mforoni.jcoder.util;

import javax.annotation.Nullable;
import com.github.mforoni.jbasic.reflect.JTypes;
import com.github.mforoni.jcoder.JField;

class MutableInferredField {
  private final String name;
  private InferredType inferredType;
  private boolean nullable;

  MutableInferredField(final String name) {
    this(name, null);
  }

  MutableInferredField(final String name, @Nullable final InferredType type) {
    this.name = name;
    this.inferredType = type;
    nullable = false;
  }

  public InferredType getInferredType() {
    return inferredType;
  }

  public void setInferredType(final InferredType inferredType) {
    this.inferredType = inferredType;
  }

  public void setNullable(final boolean value) {
    nullable = value;
  }

  public JField toJField() {
    Class<?> type = inferredType.getType();
    if (inferredType == null) {
      type = String.class;
    }
    if (nullable && type.isPrimitive()) {
      throw new IllegalStateException();
    }
    if (!nullable && JTypes.isPrimitiveOrPrimitiveWrapper(type)) {
      type = JTypes.toPrimitive(type);
    }
    final String format = inferredType.getFormats() != null && inferredType.getFormats().size() > 0
        ? inferredType.getFormats().get(0)
        : null;
    return new JField(name, type, nullable, format);
  }
}
