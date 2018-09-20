package br.com.patiolegal.dto;

import org.junit.Assert;
import org.junit.Test;
import br.com.patiolegal.dto.PartDTO.ArrestOrganBuilder;

public class ArrestOrganDTOTest {

	@Test
	public void shouldBuildWithInitials() {
		// Arrange
        String initials = "BF";

        // Act
        PartDTO dto = new ArrestOrganBuilder()
        					.withInitials(initials)
        					.build();
        
        // Assert
        Assert.assertEquals(dto.getInitials(), initials);
	}
	
	@Test
	public void shouldBuildWithDescription() {
		// Arrange
        String description = "BARRACÃO FUNDOS";

        // Act
        PartDTO dto = new ArrestOrganBuilder()
        					.withDescription(description)
        					.build();
        
        // Assert
        Assert.assertEquals(dto.getDescription(), description);
	}

}
