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
 * Immutable implementation of {@link Order}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableOrder.builder()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Generated("org.immutables.processor.ProxyProcessor")
@Immutable
final class ImmutableOrder implements Order {
  private final long id;

  private ImmutableOrder(long id) {
    this.id = id;
  }

  /**
   * @return The value of the {@code id} attribute
   */
  @Override
  public long getId() {
    return id;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Order#getId() id} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for id
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableOrder withId(long value) {
    if (this.id == value) return this;
    return new ImmutableOrder(value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableOrder} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableOrder
        && equalTo((ImmutableOrder) another);
  }

  private boolean equalTo(ImmutableOrder another) {
    return id == another.id;
  }

  /**
   * Computes a hash code from attributes: {@code id}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + Long.hashCode(id);
    return h;
  }

  /**
   * Prints the immutable value {@code Order} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "Order{"
        + "id=" + id
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link Order} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable Order instance
   */
  public static ImmutableOrder copyOf(Order instance) {
    if (instance instanceof ImmutableOrder) {
      return (ImmutableOrder) instance;
    }
    return ImmutableOrder.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableOrder ImmutableOrder}.
   * @return A new ImmutableOrder builder
   */
  public static ImmutableOrder.Builder builder() {
    return new ImmutableOrder.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableOrder ImmutableOrder}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_ID = 0x1L;
    private long initBits = 0x1L;

    private long id;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code Order} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(Order instance) {
      Objects.requireNonNull(instance, "instance");
      id(instance.getId());
      return this;
    }

    /**
     * Initializes the value for the {@link Order#getId() id} attribute.
     * @param id The value for id 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder id(long id) {
      this.id = id;
      initBits &= ~INIT_BIT_ID;
      return this;
    }

    /**
     * Builds a new {@link ImmutableOrder ImmutableOrder}.
     * @return An immutable instance of Order
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableOrder build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableOrder(id);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_ID) != 0) attributes.add("id");
      return "Cannot build Order, some of required attributes are not set " + attributes;
    }
  }
}
