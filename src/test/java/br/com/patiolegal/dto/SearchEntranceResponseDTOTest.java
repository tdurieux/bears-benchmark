package br.com.patiolegal.dto;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import br.com.patiolegal.dto.SearchEntranceResponseDTO.SearchEntranceBuilder;

public class SearchEntranceResponseDTOTest {

	@Test
	public void shouldBuildWithEntranceDate() {
		LocalDate entranceDate = LocalDate.now();
		SearchEntranceResponseDTO dto = new SearchEntranceBuilder().withEntranceDate(entranceDate).build();
		Assert.assertEquals(dto.getEntranceDate(), entranceDate);
	}

	@Test
	public void shouldBuildWithDateTimeOut() {
		LocalDate dateTimeOut = LocalDate.of(2018, 12, 23);
		SearchEntranceResponseDTO dto = new SearchEntranceBuilder().withExitDate(dateTimeOut).build();
		Assert.assertEquals(dto.getExitDate(), dateTimeOut);
	}

	@Test
	public void shouldBuildWithProtocol() {
		String protocol = "PROTOCOLO2015081520568978";
		SearchEntranceResponseDTO dto = new SearchEntranceBuilder().withProtocol(protocol).build();
		Assert.assertEquals(dto.getProtocol(), protocol);
	}

	@Test
	public void shouldBuildWithSportingPlate() {
		String sportingPlate = "AAE-9858";
		SearchEntranceResponseDTO dto = new SearchEntranceBuilder().withSportingPlate(sportingPlate).build();
		Assert.assertEquals(dto.getSportingPlate(), sportingPlate);
	}

	@Test
	public void shouldBuildWithOriginalPlate() {
		String originalPlate = "AAS-2356";
		SearchEntranceResponseDTO dto = new SearchEntranceBuilder().withOriginalPlate(originalPlate).build();
		Assert.assertEquals(dto.getOriginalPlate(), originalPlate);
	}
}
