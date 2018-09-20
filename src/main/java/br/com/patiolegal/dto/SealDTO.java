package br.com.patiolegal.dto;

import java.awt.image.BufferedImage;

public class SealDTO {

	private String protocol;
	private String authentication;
	private BufferedImage image;

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getAuthentication() {
		return authentication;
	}

	public void setAuthentication(String authentication) {
		this.authentication = authentication;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage bufferedImage) {
		this.image = bufferedImage;
	}

}
