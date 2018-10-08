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
 * Immutable implementation of {@link User}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableUser.builder()}.
 */
@SuppressWarnings({"all"})
@ParametersAreNonnullByDefault
@Generated("org.immutables.processor.ProxyProcessor")
@Immutable
final class ImmutableUser implements User {
  private final String firstName;
  private final String lastName;
  private final String nationality;
  private final @Nullable Integer age;
  private final boolean admin;

  private ImmutableUser(
      String firstName,
      String lastName,
      String nationality,
      @Nullable Integer age,
      boolean admin) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.nationality = nationality;
    this.age = age;
    this.admin = admin;
  }

  /**
   * @return The value of the {@code firstName} attribute
   */
  @Override
  public String getFirstName() {
    return firstName;
  }

  /**
   * @return The value of the {@code lastName} attribute
   */
  @Override
  public String getLastName() {
    return lastName;
  }

  /**
   * @return The value of the {@code nationality} attribute
   */
  @Override
  public String getNationality() {
    return nationality;
  }

  /**
   * @return The value of the {@code age} attribute
   */
  @Override
  public @Nullable Integer getAge() {
    return age;
  }

  /**
   * @return The value of the {@code admin} attribute
   */
  @Override
  public boolean isAdmin() {
    return admin;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#getFirstName() firstName} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for firstName
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withFirstName(String value) {
    if (this.firstName.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "firstName");
    return new ImmutableUser(newValue, this.lastName, this.nationality, this.age, this.admin);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#getLastName() lastName} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for lastName
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withLastName(String value) {
    if (this.lastName.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "lastName");
    return new ImmutableUser(this.firstName, newValue, this.nationality, this.age, this.admin);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#getNationality() nationality} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for nationality
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withNationality(String value) {
    if (this.nationality.equals(value)) return this;
    String newValue = Objects.requireNonNull(value, "nationality");
    return new ImmutableUser(this.firstName, this.lastName, newValue, this.age, this.admin);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#getAge() age} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for age (can be {@code null})
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withAge(@Nullable Integer value) {
    if (Objects.equals(this.age, value)) return this;
    return new ImmutableUser(this.firstName, this.lastName, this.nationality, value, this.admin);
  }

  /**
   * Copy the current immutable object by setting a value for the {@link User#isAdmin() admin} attribute.
   * A value equality check is used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for admin
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableUser withAdmin(boolean value) {
    if (this.admin == value) return this;
    return new ImmutableUser(this.firstName, this.lastName, this.nationality, this.age, value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableUser} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(@Nullable Object another) {
    if (this == another) return true;
    return another instanceof ImmutableUser
        && equalTo((ImmutableUser) another);
  }

  private boolean equalTo(ImmutableUser another) {
    return firstName.equals(another.firstName)
        && lastName.equals(another.lastName)
        && nationality.equals(another.nationality)
        && Objects.equals(age, another.age)
        && admin == another.admin;
  }

  /**
   * Computes a hash code from attributes: {@code firstName}, {@code lastName}, {@code nationality}, {@code age}, {@code admin}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + firstName.hashCode();
    h += (h << 5) + lastName.hashCode();
    h += (h << 5) + nationality.hashCode();
    h += (h << 5) + Objects.hashCode(age);
    h += (h << 5) + Boolean.hashCode(admin);
    return h;
  }

  /**
   * Prints the immutable value {@code User} with attribute values.
   * @return A string representation of the value
   */
  @Override
  public String toString() {
    return "User{"
        + "firstName=" + firstName
        + ", lastName=" + lastName
        + ", nationality=" + nationality
        + ", age=" + age
        + ", admin=" + admin
        + "}";
  }

  /**
   * Creates an immutable copy of a {@link User} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable User instance
   */
  public static ImmutableUser copyOf(User instance) {
    if (instance instanceof ImmutableUser) {
      return (ImmutableUser) instance;
    }
    return ImmutableUser.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableUser ImmutableUser}.
   * @return A new ImmutableUser builder
   */
  public static ImmutableUser.Builder builder() {
    return new ImmutableUser.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableUser ImmutableUser}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @NotThreadSafe
  public static final class Builder {
    private static final long INIT_BIT_FIRST_NAME = 0x1L;
    private static final long INIT_BIT_LAST_NAME = 0x2L;
    private static final long INIT_BIT_NATIONALITY = 0x4L;
    private static final long INIT_BIT_ADMIN = 0x8L;
    private long initBits = 0xfL;

    private @Nullable String firstName;
    private @Nullable String lastName;
    private @Nullable String nationality;
    private @Nullable Integer age;
    private boolean admin;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code User} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(User instance) {
      Objects.requireNonNull(instance, "instance");
      firstName(instance.getFirstName());
      lastName(instance.getLastName());
      nationality(instance.getNationality());
      @Nullable Integer ageValue = instance.getAge();
      if (ageValue != null) {
        age(ageValue);
      }
      admin(instance.isAdmin());
      return this;
    }

    /**
     * Initializes the value for the {@link User#getFirstName() firstName} attribute.
     * @param firstName The value for firstName 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder firstName(String firstName) {
      this.firstName = Objects.requireNonNull(firstName, "firstName");
      initBits &= ~INIT_BIT_FIRST_NAME;
      return this;
    }

    /**
     * Initializes the value for the {@link User#getLastName() lastName} attribute.
     * @param lastName The value for lastName 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder lastName(String lastName) {
      this.lastName = Objects.requireNonNull(lastName, "lastName");
      initBits &= ~INIT_BIT_LAST_NAME;
      return this;
    }

    /**
     * Initializes the value for the {@link User#getNationality() nationality} attribute.
     * @param nationality The value for nationality 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder nationality(String nationality) {
      this.nationality = Objects.requireNonNull(nationality, "nationality");
      initBits &= ~INIT_BIT_NATIONALITY;
      return this;
    }

    /**
     * Initializes the value for the {@link User#getAge() age} attribute.
     * @param age The value for age (can be {@code null})
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder age(@Nullable Integer age) {
      this.age = age;
      return this;
    }

    /**
     * Initializes the value for the {@link User#isAdmin() admin} attribute.
     * @param admin The value for admin 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder admin(boolean admin) {
      this.admin = admin;
      initBits &= ~INIT_BIT_ADMIN;
      return this;
    }

    /**
     * Builds a new {@link ImmutableUser ImmutableUser}.
     * @return An immutable instance of User
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableUser build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableUser(firstName, lastName, nationality, age, admin);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_FIRST_NAME) != 0) attributes.add("firstName");
      if ((initBits & INIT_BIT_LAST_NAME) != 0) attributes.add("lastName");
      if ((initBits & INIT_BIT_NATIONALITY) != 0) attributes.add("nationality");
      if ((initBits & INIT_BIT_ADMIN) != 0) attributes.add("admin");
      return "Cannot build User, some of required attributes are not set " + attributes;
    }
  }
}
