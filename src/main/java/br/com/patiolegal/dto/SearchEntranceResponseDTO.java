package br.com.patiolegal.dto;

import java.time.LocalDate;

public class SearchEntranceResponseDTO {
    
	private LocalDate entranceDate;
	private LocalDate exitDate;
	private String protocol;
	private String sportingPlate;
	private String originalPlate;

	public LocalDate getEntranceDate() {
		return entranceDate;
	}

	public LocalDate getExitDate() {
		return exitDate;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getSportingPlate() {
		return sportingPlate;
	}

	public String getOriginalPlate() {
		return originalPlate;
	}

	public static class SearchEntranceBuilder {
		private SearchEntranceResponseDTO dto;

		public SearchEntranceBuilder() {
			dto = new SearchEntranceResponseDTO();
		}

		public SearchEntranceBuilder withEntranceDate(LocalDate entranceDate) {
			dto.entranceDate = entranceDate;
			return this;
		}

		public SearchEntranceBuilder withExitDate(LocalDate exitDate) {
			dto.exitDate = exitDate;
			return this;
		}

		public SearchEntranceBuilder withProtocol(String protocol) {
			dto.protocol = protocol;
			return this;
		}

		public SearchEntranceBuilder withSportingPlate(String sportingPlate) {
			dto.sportingPlate = sportingPlate;
			return this;
		}

		public SearchEntranceBuilder withOriginalPlate(String originalPlate) {
			dto.originalPlate = originalPlate;
			return this;
		}

		public SearchEntranceResponseDTO build() {
			return dto;
		}
	}
}