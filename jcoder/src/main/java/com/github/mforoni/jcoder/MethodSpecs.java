package com.github.mforoni.jcoder;

import javax.lang.model.element.Modifier;
import com.github.mforoni.jbasic.reflect.JMethods;
import com.squareup.javapoet.MethodSpec;

/**
 * @author Foroni Marco
 */
public final class MethodSpecs {
  private MethodSpecs() {
    throw new AssertionError();
  }

  public static MethodSpec getterMethodSpec(final JField field) {
    return getterMethodSpec(field.getName(), field.getType(), field.isNullable());
  }

  public static MethodSpec getterMethodSpec(final String fieldName, final Class<?> type,
      final boolean nullable) {
    final MethodSpec.Builder methodBuilder =
        MethodSpec.methodBuilder(JMethods.getterName(fieldName, type)).addModifiers(Modifier.PUBLIC)
            .returns(type);
    // if (type.equals(String.class)) {
    // methodBuilder.addAnnotation(nullable ? Nullable.class : Nonnull.class);
    // }
    return methodBuilder.addStatement("return $N", fieldName).build();
  }

  public static MethodSpec setterMethodSpec(final JField field) {
    return setterMethodSpec(field.getName(), field.getType());
  }

  public static MethodSpec setterMethodSpec(final String fieldName, final Class<?> type) {
    return MethodSpec.methodBuilder(JMethods.setterName(fieldName)).addModifiers(Modifier.PUBLIC)
        .addParameter(type, fieldName, Modifier.FINAL)
        .addStatement("this.$N=$N", fieldName, fieldName).build();
  }
}
