package org.vea.tutorial.controllers.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Immutable implementation of {@link Father}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableFather.builder()}.
 * Use the static factory method to create immutable instances:
 * {@code ImmutableFather.of()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Generated("org.immutables.processor.ProxyProcessor")
@Immutable
public final class ImmutableFather implements Father {
  private final String name;

  private ImmutableFather(String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  private ImmutableFather(ImmutableFather original, String name) {
    this.name = name;
  }

  /**
   * @return The value of the {@code name} attribute
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Father#getName() name} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for name
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableFather withName(String value) {
    if (this.name.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "name");
    return new ImmutableFather(this, newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableFather} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableFather
        && equalTo((ImmutableFather) another);
  }

  private boolean equalTo(ImmutableFather another) {
    return name.equals(another.name);
  }

  /**
   * Computes a hash code from attributes: {@code name}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + name.hashCode();
    return h;
  }

  /**
   * Prints the immutable value {@code Father} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "Father{"
        + "name=" + name
        + "}";
  }

  /**
   * Construct a new immutable {@code Father} instance.
   * @param name The value for the {@code name} attribute
   * @return An immutable Father instance
   */
  public static ImmutableFather of(String name) {
    return new ImmutableFather(name);
  }

  /**
   * Creates an immutable copy of a {@link Father} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable Father instance
   */
  public static ImmutableFather copyOf(Father instance) {
    if (instance instanceof ImmutableFather) {
      return (ImmutableFather) instance;
    }
    return ImmutableFather.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableFather ImmutableFather}.
   * @return A new ImmutableFather builder
   */
  public static ImmutableFather.Builder builder() {
    return new ImmutableFather.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableFather ImmutableFather}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_NAME = 0x1L;
    private long initBits = 0x1L;

    private @Nullable String name;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code Father} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(Father instance) {
      Objects.requireNonNull(instance, "instance");
      name(instance.getName());
      return this;
    }

    /**
     * Initializes the value for the {@link Father#getName() name} attribute.
     * @param name The value for name 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder name(String name) {
      this.name = Objects.requireNonNull(name, "name");
      initBits &= ~INIT_BIT_NAME;
      return this;
    }

    /**
     * Builds a new {@link ImmutableFather ImmutableFather}.
     * @return An immutable instance of Father
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableFather build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableFather(null, name);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_NAME) != 0) attributes.add("name");
      return "Cannot build Father, some of required attributes are not set " + attributes;
    }
  }
}
