package br.com.patiolegal.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Exit {

	private LocalDate date;
	private LocalDateTime dateTimeOut = LocalDateTime.now();
	private String username;
	private String taxId;
	private String name;

	public Exit(LocalDate date, String taxId, String name) {
		this.date = date;
		this.taxId = taxId;
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setTaxId(String taxId) {
		this.taxId = taxId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDateTime getDateTimeOut() {
		return dateTimeOut;
	}

	public String getUsername() {
		return username;
	}

	public String getTaxId() {
		return taxId;
	}

	public String getName() {
		return name;
	}

}
