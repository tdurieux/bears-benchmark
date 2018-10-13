package com.github.mforoni.jcoder.demo.generated;

import java.io.Serializable;
import java.lang.Override;
import java.lang.String;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;

/**
 * Auto generated from file student.csv */
public class Student implements Serializable {
	public static final CellProcessor[] CELL_PROCESSOR = { //
	new NotNull(new ParseInt()), // id
	new NotNull(), // firstName
	new NotNull(), // lastName
	new NotNull(), // gender
	new NotNull(new ParseInt()), // age
	};

	private int id;

	private String firstName;

	private String lastName;

	private String gender;

	private int age;

	public Student() {
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id=id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName=firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName=lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(final String gender) {
		this.gender=gender;
	}

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		this.age=age;
	}

	@Override
	public String toString() {
		return String.format("Student [id=%s, firstName=%s, lastName=%s, gender=%s, age=%s]", id, firstName, lastName, gender, age);
	}
}
