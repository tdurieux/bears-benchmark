package br.com.patiolegal.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

public class ConfigurationRequestDTO {

	@NotBlank
	private String key;
	private String value;
	private LocalDateTime modificationDate = LocalDateTime.now();

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public LocalDateTime getModificationDate() {
		return modificationDate;
	}

	@Override
	public String toString() {
		return "ConfigurationRequestDTO [key=" + key + ", value=" + value + ", modificationDate=" + modificationDate
				+ "]";
	}

}
