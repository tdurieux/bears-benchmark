package br.com.patiolegal.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

public class ExitRequestDTO {

	@NotNull
	private LocalDate date;
	@NotBlank
	@CPF(message = "CPF inválido")
	private String taxIdentifier;
	@NotBlank
	private String name;
	@NotBlank
	private String protocol;

	public LocalDate getDate() {
		return date;
	}

	public String getTaxIdentifier() {
		return taxIdentifier;
	}

	public String getName() {
		return name;
	}

	public String getProtocol() {
		return protocol;
	}

	@Override
	public String toString() {
		return "ExitRequestDTO [date=" + date + ", taxIdentifier=" + taxIdentifier + ", name=" + name
				+ ", protocol=" + protocol + "]";
	}

}
