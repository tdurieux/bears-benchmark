package br.com.patiolegal.dto;

public class LoginResponseDTO {

    private String accessToken;
    private String profile;
    private String username;
    
    public static class LoginResponseBuilder {
        
        private LoginResponseDTO dto;
        
        public LoginResponseBuilder() {
            dto = new LoginResponseDTO();
        }
        
        public LoginResponseBuilder withAccessToken(String accessToken) {
            dto.accessToken = accessToken;
            return this;
        }
        
        public LoginResponseBuilder withProfile(String profile) {
            dto.profile = profile;
            return this;
        }
        
        public LoginResponseBuilder withUsername(String username) {
            dto.username = username;
            return this;
        }
        
        public LoginResponseDTO build() {
            return dto;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getProfile() {
        return profile;
    }

    public String getUsername() {
        return username;
    }

}
