package br.com.patiolegal.dto;

import java.time.LocalDate;

public class SearchEntranceRequestDTO {
	private String protocol;
	private LocalDate startDate;
	private LocalDate endDate;

	public String getProtocol() {
		return protocol;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	@Override
	public String toString() {
		return "SearchEntranceRequestDTO [protocol=" + protocol + ", startDate=" + startDate + ", endDate="
				+ endDate + "]";
	}

}