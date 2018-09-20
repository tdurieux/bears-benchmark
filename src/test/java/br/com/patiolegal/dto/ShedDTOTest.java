package br.com.patiolegal.dto;

import org.junit.Assert;
import org.junit.Test;

import br.com.patiolegal.dto.ShedDTO.ShedBuilder;

public class ShedDTOTest {

	@Test
	public void shouldBuildWithInitials() {
		String initials = "FF";

		ShedDTO dto = new ShedBuilder().withInitials(initials).build();

		Assert.assertEquals(dto.getInitials(), initials);
	}

	@Test
	public void shouldBuildWithDescription() {
		String description = "Fundos";

		ShedDTO dto = new ShedBuilder().withDescription(description).build();

		Assert.assertEquals(dto.getDescription(), description);
	}

}
