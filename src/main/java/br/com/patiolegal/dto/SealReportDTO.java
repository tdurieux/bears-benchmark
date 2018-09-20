package br.com.patiolegal.dto;

public class SealReportDTO {

    private String location;
    private String authentication;
    private String protocol;

    public static class SealReportBuilder {

        private SealReportDTO dto;

        public SealReportBuilder() {
            dto = new SealReportDTO();
        }

        public SealReportBuilder withLocation(String location) {
            dto.location = location;
            return this;
        }

        public SealReportBuilder withAuthentication(String authentication) {
            dto.authentication = authentication;
            return this;
        }

        public SealReportBuilder withProtocol(String protocol) {
            dto.protocol = protocol;
            return this;
        }

        public SealReportDTO build() {
            return dto;
        }

    }

    public String getLocation() {
        return location;
    }

    public String getAuthentication() {
        return authentication;
    }

    public String getProtocol() {
        return protocol;
    }

    public String stringfy() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(protocol);
        stringBuilder.append("\n");
        stringBuilder.append(location);
        stringBuilder.append("\n");
        stringBuilder.append(authentication);
        return String.valueOf(stringBuilder);
    }

}
