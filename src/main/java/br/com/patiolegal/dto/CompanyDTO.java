package br.com.patiolegal.dto;

import org.apache.commons.lang.StringUtils;

public class CompanyDTO {

    private String name;
    private String socialName;
    private String publicPlace;
    private String postalCode;
    private String city;
    private String state;
    private String phone;
    private String image;

    public String getLine1() {
        return StringUtils.upperCase(name);
    }

    public String getLine2() {
        StringBuilder sb = new StringBuilder();
        sb.append("Endereço: ");
        sb.append(publicPlace);
        String line2 = String.valueOf(sb);
        return StringUtils.upperCase(line2);
    }

    public String getLine3() {
        StringBuilder sb = new StringBuilder();
        sb.append("CEP: ");
        sb.append(postalCode);
        sb.append(" - ");
        sb.append(city);
        sb.append(" - ");
        sb.append(state);
        sb.append(" - TELEFONE: ");
        sb.append(phone);
        String line3 = String.valueOf(sb);
        return StringUtils.upperCase(line3);
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

    public static class CompanyDTOBuilder {

        CompanyDTO dto;

        public CompanyDTOBuilder() {
            dto = new CompanyDTO();
        }

        public CompanyDTOBuilder withName(String name) {
            dto.name = name;
            return this;
        }

        public CompanyDTOBuilder withSocialName(String socialName) {
            dto.socialName = socialName;
            return this;
        }

        public CompanyDTOBuilder withPublicPlace(String publicPlace) {
            dto.publicPlace = publicPlace;
            return this;
        }

        public CompanyDTOBuilder withPostalCode(String postalCode) {
            dto.postalCode = postalCode;
            return this;
        }

        public CompanyDTOBuilder withCity(String city) {
            dto.city = city;
            return this;
        }

        public CompanyDTOBuilder withState(String state) {
            dto.state = state;
            return this;
        }

        public CompanyDTOBuilder withPhone(String phone) {
            dto.phone = phone;
            return this;
        }

        public CompanyDTOBuilder withImage(String image) {
            dto.image = image;
            return this;
        }

        public CompanyDTO build() {
            return dto;
        }

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((city == null) ? 0 : city.hashCode());
        result = prime * result + ((image == null) ? 0 : image.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((phone == null) ? 0 : phone.hashCode());
        result = prime * result + ((postalCode == null) ? 0 : postalCode.hashCode());
        result = prime * result + ((publicPlace == null) ? 0 : publicPlace.hashCode());
        result = prime * result + ((socialName == null) ? 0 : socialName.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CompanyDTO)) {
            return false;
        }
        CompanyDTO other = (CompanyDTO) obj;
        if (city == null) {
            if (other.city != null) {
                return false;
            }
        } else if (!city.equals(other.city)) {
            return false;
        }
        if (image == null) {
            if (other.image != null) {
                return false;
            }
        } else if (!image.equals(other.image)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (phone == null) {
            if (other.phone != null) {
                return false;
            }
        } else if (!phone.equals(other.phone)) {
            return false;
        }
        if (postalCode == null) {
            if (other.postalCode != null) {
                return false;
            }
        } else if (!postalCode.equals(other.postalCode)) {
            return false;
        }
        if (publicPlace == null) {
            if (other.publicPlace != null) {
                return false;
            }
        } else if (!publicPlace.equals(other.publicPlace)) {
            return false;
        }
        if (socialName == null) {
            if (other.socialName != null) {
                return false;
            }
        } else if (!socialName.equals(other.socialName)) {
            return false;
        }
        if (state == null) {
            if (other.state != null) {
                return false;
            }
        } else if (!state.equals(other.state)) {
            return false;
        }
        return true;
    }

}
