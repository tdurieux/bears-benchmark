package br.com.patiolegal.dto;

import org.junit.Assert;
import org.junit.Test;

import br.com.patiolegal.dto.CompanyDTO.CompanyDTOBuilder;

public class CompanyDTOTest {

	@Test
	public void shouldBuilderWithName() {

		String name = "Empresa S.A";

		CompanyDTO dto = new CompanyDTOBuilder().withName(name).build();

		Assert.assertEquals(dto.getName(), name);

	}

	@Test
	public void shouldBuilderWithSocialName() {

		String socialName = "Patio Legal";

		CompanyDTO dto = new CompanyDTOBuilder().withSocialName(socialName).build();

		Assert.assertEquals(dto.getSocialName(), socialName);
	}

	@Test
	public void shouldBuilderWithPublicPlace() {

		String publicPlace = "Praça do Centro";

		CompanyDTO dto = new CompanyDTOBuilder().withPublicPlace(publicPlace).build();

		Assert.assertEquals(dto.getPublicPlace(), publicPlace);
	}

	@Test
	public void shouldBuilderWithPostalCode() {

		String postalCode = "05045-040";

		CompanyDTO dto = new CompanyDTOBuilder().withPostalCode(postalCode).build();

		Assert.assertEquals(dto.getPostalCode(), postalCode);
	}

	@Test
	public void shouldBuilderWithCity() {

		String city = "Maringá";

		CompanyDTO dto = new CompanyDTOBuilder().withCity(city).build();

		Assert.assertEquals(dto.getCity(), city);
	}

	@Test
	public void shouldBuilderWithState() {

		String state = "Paraná";

		CompanyDTO dto = new CompanyDTOBuilder().withState(state).build();

		Assert.assertEquals(dto.getState(), state);
	}

	@Test
	public void shouldBuilderWithPhone() {

		String phone = "44 3220-5898";

		CompanyDTO dto = new CompanyDTOBuilder().withPhone(phone).build();

		Assert.assertEquals(dto.getPhone(), phone);
	}

	@Test
	public void shouldBuilderWithImage() {

		String image = "kasjfnfofggherognsdvaslfjsdfiojsafkdadlfmsadmfasfio";

		CompanyDTO dto = new CompanyDTOBuilder().withImage(image).build();

		Assert.assertEquals(dto.getImage(), image);
	}

}
