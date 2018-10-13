package com.github.mforoni.jcoder.util;

import javax.annotation.Nullable;
import com.github.mforoni.jbasic.reflect.JTypes;
import com.github.mforoni.jcoder.JField;

class MutableField {
  private final String name;
  private Class<?> type;
  private boolean nullable;

  MutableField(final String name) {
    this(name, null);
  }

  MutableField(final String name, @Nullable final Class<?> type) {
    this.name = name;
    this.type = type;
    nullable = false;
  }

  public void setNullable(final boolean value) {
    nullable = value;
  }

  public void setType(@Nullable final Class<?> type) {
    if (this.type == null || this.type == type) {
      this.type = type;
    } else {
      throw new IllegalStateException("Type can't change from " + this.type + " to " + type);
    }
  }

  public JField toJField() {
    if (type == null) {
      type = String.class;
    }
    if (nullable && type.isPrimitive()) {
      throw new IllegalStateException();
    }
    if (!nullable && JTypes.isPrimitiveOrPrimitiveWrapper(type)) {
      type = JTypes.toPrimitive(type);
    }
    return new JField(name, type, nullable);
  }
}
