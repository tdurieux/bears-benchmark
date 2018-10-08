package org.vea.tutorial.controllers.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Immutable implementation of {@link Product}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableProduct.builder()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Generated("org.immutables.processor.ProxyProcessor")
@Immutable
final class ImmutableProduct implements Product {
  private final String name;
  private final BigDecimal price;
  private final boolean stock;

  private ImmutableProduct(String name, BigDecimal price, boolean stock) {
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  /**
   * @return The value of the {@code name} attribute
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * @return The value of the {@code price} attribute
   */
  @Override
  public BigDecimal getPrice() {
    return price;
  }

  /**
   * @return The value of the {@code stock} attribute
   */
  @Override
  public boolean isStock() {
    return stock;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Product#getName() name} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for name
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableProduct withName(String value) {
    if (this.name.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "name");
    return new ImmutableProduct(newValue, this.price, this.stock);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Product#getPrice() price} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for price
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableProduct withPrice(BigDecimal value) {
    if (this.price.equals(value)) return this;
    BigDecimal newValue = Objects.requireNonNull(value, "price");
    return new ImmutableProduct(this.name, newValue, this.stock);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link Product#isStock() stock} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for stock
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableProduct withStock(boolean value) {
    if (this.stock == value) return this;
    return new ImmutableProduct(this.name, this.price, value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableProduct} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableProduct
        && equalTo((ImmutableProduct) another);
  }

  private boolean equalTo(ImmutableProduct another) {
    return name.equals(another.name)
        && price.equals(another.price)
        && stock == another.stock;
  }

  /**
   * Computes a hash code from attributes: {@code name}, {@code price}, {@code stock}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + name.hashCode();
    h += (h << 5) + price.hashCode();
    h += (h << 5) + Boolean.hashCode(stock);
    return h;
  }

  /**
   * Prints the immutable value {@code Product} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "Product{"
        + "name=" + name
        + ", price=" + price
        + ", stock=" + stock
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link Product} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable Product instance
   */
  public static ImmutableProduct copyOf(Product instance) {
    if (instance instanceof ImmutableProduct) {
      return (ImmutableProduct) instance;
    }
    return ImmutableProduct.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableProduct ImmutableProduct}.
   * @return A new ImmutableProduct builder
   */
  public static ImmutableProduct.Builder builder() {
    return new ImmutableProduct.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableProduct ImmutableProduct}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_NAME = 0x1L;
    private static final long INIT_BIT_PRICE = 0x2L;
    private static final long INIT_BIT_STOCK = 0x4L;
    private long initBits = 0x7L;

    private @Nullable String name;
    private @Nullable BigDecimal price;
    private boolean stock;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code Product} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(Product instance) {
      Objects.requireNonNull(instance, "instance");
      name(instance.getName());
      price(instance.getPrice());
      stock(instance.isStock());
      return this;
    }

    /**
     * Initializes the value for the {@link Product#getName() name} attribute.
     * @param name The value for name 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder name(String name) {
      this.name = Objects.requireNonNull(name, "name");
      initBits &= ~INIT_BIT_NAME;
      return this;
    }

    /**
     * Initializes the value for the {@link Product#getPrice() price} attribute.
     * @param price The value for price 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder price(BigDecimal price) {
      this.price = Objects.requireNonNull(price, "price");
      initBits &= ~INIT_BIT_PRICE;
      return this;
    }

    /**
     * Initializes the value for the {@link Product#isStock() stock} attribute.
     * @param stock The value for stock 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder stock(boolean stock) {
      this.stock = stock;
      initBits &= ~INIT_BIT_STOCK;
      return this;
    }

    /**
     * Builds a new {@link ImmutableProduct ImmutableProduct}.
     * @return An immutable instance of Product
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableProduct build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableProduct(name, price, stock);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_NAME) != 0) attributes.add("name");
      if ((initBits & INIT_BIT_PRICE) != 0) attributes.add("price");
      if ((initBits & INIT_BIT_STOCK) != 0) attributes.add("stock");
      return "Cannot build Product, some of required attributes are not set " + attributes;
    }
  }
}
