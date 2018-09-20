package br.com.patiolegal.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document (collection = "part")
@TypeAlias("part")
public class Part {
	
	@Id
	private String id;
	private String initials;
	private String description;
	
	public String getInitials() {
		return initials;
	}
	
	public String getDescription() {
		return description;
	}

}
