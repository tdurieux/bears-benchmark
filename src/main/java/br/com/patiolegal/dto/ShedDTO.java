package br.com.patiolegal.dto;

public class ShedDTO {
	
	private String initials;
	private String description;
	
	public static class ShedBuilder{
		
		private ShedDTO dto;
		
		public ShedBuilder() {
			dto = new ShedDTO();
		}
		
		public ShedBuilder withInitials(String initials){
			dto.initials = initials;
			return this;
		}
		
		public ShedBuilder withDescription(String description){
			dto.description = description;
			return this;
		}
		
		public ShedDTO build(){
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
