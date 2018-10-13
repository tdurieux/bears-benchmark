package com.github.mforoni.jcoder.demo.generated;

import javax.annotation.concurrent.Immutable;
import org.joda.time.LocalDate;

/**
 * Auto generated
 */
@Immutable
public final class Person {
  private final String firstName;
  private final String lastName;
  private final LocalDate dateBirth;
  private final String email;

  public Person(final String firstName, final String lastName, final LocalDate dateBirth,
      final String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.dateBirth = dateBirth;
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public LocalDate getDateBirth() {
    return dateBirth;
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String toString() {
    return String.format("Person [firstName=%s, lastName=%s, dateBirth=%s, email=%s]", firstName,
        lastName, dateBirth, email);
  }
}
