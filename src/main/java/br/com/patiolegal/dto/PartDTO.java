package br.com.patiolegal.dto;

public class PartDTO {
	
	private String initials;
	private String description;
	
	public static class ArrestOrganBuilder{
		
		private PartDTO dto;
		
		public ArrestOrganBuilder() {
			dto = new PartDTO();
		}
		
		public ArrestOrganBuilder withInitials(String initials) {
			dto.initials = initials;
			return this;
		}
		
		public ArrestOrganBuilder withDescription(String description) {
			dto.description = description;
			return this;
		}
		
		public PartDTO build() {
			return dto;
		}
		
	}
	
	public String getInitials() {
		return initials;
	}
	
	public String getDescription() {
		return description;
	}

}
