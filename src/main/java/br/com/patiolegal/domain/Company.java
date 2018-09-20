package br.com.patiolegal.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
public class Company {

	@Id
	private String id;
	private String name;
	private String socialName;
	private String publicPlace;
	private String postalCode;
	private String city;
	private String state;
	private String phone;
	private String image;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getSocialName() {
		return socialName;
	}

	public String getPublicPlace() {
		return publicPlace;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getPhone() {
		return phone;
	}

	public String getImage() {
		return image;
	}

    public void setCity(String city) {
        this.city = city;
    }

}
