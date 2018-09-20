package br.com.patiolegal.dto;

import org.junit.Assert;
import org.junit.Test;

import br.com.patiolegal.dto.LoginResponseDTO.LoginResponseBuilder;

public class LoginResponseDTOTest {

	@Test
	public void shouldBuilderWithAccessToken() {

		String accessToken = "as98daslk54218asjej7e4x1c6f";

		LoginResponseDTO dto = new LoginResponseBuilder().withAccessToken(accessToken).build();

		Assert.assertEquals(dto.getAccessToken(), accessToken);
	}

	@Test
	public void shouldBuilderWithProfile() {

		String profile = "ADMIN";

		LoginResponseDTO dto = new LoginResponseBuilder().withProfile(profile).build();

		Assert.assertEquals(dto.getProfile(), profile);
	}

	@Test
	public void shouldBuilderWithUsername() {

		String username = "Bolota";

		LoginResponseDTO dto = new LoginResponseBuilder().withUsername(username).build();

		Assert.assertEquals(dto.getUsername(), username);
	}

}
