package br.com.patiolegal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.patiolegal.domain.ChassisState;
import br.com.patiolegal.domain.EngineState;

public class ProtocolDTO {

    private String part;
    private String protocol;
    private LocalDate date;
    private LocalDateTime dateTimeIn;
    private LocalDateTime dateTimeOut;
    private String policeInvestigation;
    private String eventBulletin;
    private String taxId;
    private String name;
    private String theyRenamed;
    private String ownerName;
    private String ownerTaxIdentifier;
    private String brand;
    private String model;
    private String category;
    private String color;
    private String fuel;
    private Integer yearFactory;
    private Integer yearModel;
    private String sportingPlate;
    private String originalPlate;
    private ChassisState chassisState;
    private String chassis;
    private EngineState motorState;
    private String motor;
    private Boolean insured;
    private Boolean financed;
    private Boolean stolen;
    private Boolean drugTrafficking;
    private Boolean moneyLaundry;
    private Boolean perquisite;
    private Boolean papillaryExpertise;
    private Boolean ownerIntimate;
    private Boolean authorizedAlienation;
    private Boolean debits;
    private String shed;
    private String row;
    private String column;
    private String floor;
    private String authentication;
    private Integer amountSeals;
    private String arrestOrgan;
    private String accountableOut;
    private String accountableIn;

    public String getPart() {
        return part;
    }

    public String getProtocol() {
        return protocol;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalDateTime getDateTimeIn() {
        return dateTimeIn;
    }

    public LocalDateTime getDateTimeOut() {
        return dateTimeOut;
    }

    public String getPoliceInvestigation() {
        return policeInvestigation;
    }

    public String getEventBulletin() {
        return eventBulletin;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getName() {
        return name;
    }

    public String getTheyRenamed() {
        return theyRenamed;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerTaxIdentifier() {
        return ownerTaxIdentifier;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getFuel() {
        return fuel;
    }

    public Integer getYearFactory() {
        return yearFactory;
    }

    public Integer getYearModel() {
        return yearModel;
    }

    public String getSportingPlate() {
        return sportingPlate;
    }

    public String getOriginalPlate() {
        return originalPlate;
    }

    public ChassisState getChassisState() {
        return chassisState;
    }

    public String getChassis() {
        return chassis;
    }

    public EngineState getMotorState() {
        return motorState;
    }

    public String getMotor() {
        return motor;
    }

    public Boolean getInsured() {
        return insured;
    }

    public Boolean getFinanced() {
        return financed;
    }

    public Boolean getStolen() {
        return stolen;
    }

    public Boolean getDrugTrafficking() {
        return drugTrafficking;
    }

    public Boolean getMoneyLaundry() {
        return moneyLaundry;
    }

    public Boolean getPerquisite() {
        return perquisite;
    }

    public Boolean getPapillaryExpertise() {
        return papillaryExpertise;
    }

    public Boolean getOwnerIntimate() {
        return ownerIntimate;
    }

    public Boolean getAuthorizedAlienation() {
        return authorizedAlienation;
    }

    public Boolean getDebits() {
        return debits;
    }

    public String getShed() {
        return shed;
    }

    public String getRow() {
        return row;
    }

    public String getColumn() {
        return column;
    }

    public String getFloor() {
        return floor;
    }

    public String getAuthentication() {
        return authentication;
    }

    public Integer getAmountSeals() {
        return amountSeals;
    }

    public String getArrestOrgan() {
        return arrestOrgan;
    }

    public String getAccountableOut() {
        return accountableOut;
    }

    public String getAccountableIn() {
        return accountableIn;
    }

    public static class ProtocolDTOBuilder {

        private ProtocolDTO dto;

        public ProtocolDTOBuilder() {

            dto = new ProtocolDTO();

        }

        public ProtocolDTOBuilder withPart(String part) {
            dto.part = part;
            return this;
        }

        public ProtocolDTOBuilder withProtocol(String protocol) {
            dto.protocol = protocol;
            return this;
        }

        public ProtocolDTOBuilder withDate(LocalDate date) {
            dto.date = date;
            return this;
        }

        public ProtocolDTOBuilder withDateTimeIn(LocalDateTime dateTimeIn) {
            dto.dateTimeIn = dateTimeIn;
            return this;
        }

        public ProtocolDTOBuilder withDateTimeOut(LocalDateTime dateTimeOut) {
            dto.dateTimeOut = dateTimeOut;
            return this;
        }

        public ProtocolDTOBuilder withPoliceInvestigation(String policeInvestigation) {
            dto.policeInvestigation = policeInvestigation;
            return this;
        }

        public ProtocolDTOBuilder withEventBulletin(String eventBulletin) {
            dto.eventBulletin = eventBulletin;
            return this;
        }

        public ProtocolDTOBuilder withTaxId(String taxId) {
            dto.taxId = taxId;
            return this;
        }

        public ProtocolDTOBuilder withName(String name) {
            dto.name = name;
            return this;
        }

        public ProtocolDTOBuilder withTheyRenamed(String theyRenamed) {
            dto.theyRenamed = theyRenamed;
            return this;
        }

        public ProtocolDTOBuilder withOwnerName(String ownerName) {
            dto.ownerName = ownerName;
            return this;
        }

        public ProtocolDTOBuilder withOwnerTaxIdentifier(String ownerTaxIdentifier) {
            dto.ownerTaxIdentifier = ownerTaxIdentifier;
            return this;
        }

        public ProtocolDTOBuilder withBrand(String brand) {
            dto.brand = brand;
            return this;
        }

        public ProtocolDTOBuilder withModel(String model) {
            dto.model = model;
            return this;
        }

        public ProtocolDTOBuilder withCategory(String category) {
            dto.category = category;
            return this;
        }

        public ProtocolDTOBuilder withColor(String color) {
            dto.color = color;
            return this;
        }

        public ProtocolDTOBuilder withFuel(String fuel) {
            dto.fuel = fuel;
            return this;
        }

        public ProtocolDTOBuilder withYearFactory(Integer yearFactory) {
            dto.yearFactory = yearFactory;
            return this;
        }

        public ProtocolDTOBuilder withYearModel(Integer yearModel) {
            dto.yearModel = yearModel;
            return this;
        }

        public ProtocolDTOBuilder withSportingPlate(String sportingPlate) {
            dto.sportingPlate = sportingPlate;
            return this;
        }

        public ProtocolDTOBuilder withOriginalPlate(String originalPlate) {
            dto.originalPlate = originalPlate;
            return this;
        }

        public ProtocolDTOBuilder withChassisState(ChassisState chassisState) {
            dto.chassisState = chassisState;
            return this;
        }

        public ProtocolDTOBuilder withChassis(String chassis) {
            dto.chassis = chassis;
            return this;
        }

        public ProtocolDTOBuilder withMotorState(EngineState motorState) {
            dto.motorState = motorState;
            return this;
        }

        public ProtocolDTOBuilder withMotor(String motor) {
            dto.motor = motor;
            return this;
        }

        public ProtocolDTOBuilder withInsured(Boolean insured) {
            dto.insured = insured;
            return this;
        }

        public ProtocolDTOBuilder withFinanced(Boolean financed) {
            dto.financed = financed;
            return this;
        }

        public ProtocolDTOBuilder withStolen(Boolean stolen) {
            dto.stolen = stolen;
            return this;
        }

        public ProtocolDTOBuilder withDrugTrafficking(Boolean drugTrafficking) {
            dto.drugTrafficking = drugTrafficking;
            return this;
        }

        public ProtocolDTOBuilder withMoneyLaundry(Boolean moneyLaundry) {
            dto.moneyLaundry = moneyLaundry;
            return this;
        }

        public ProtocolDTOBuilder withPerquisite(Boolean perquisite) {
            dto.perquisite = perquisite;
            return this;
        }

        public ProtocolDTOBuilder withPapillaryExpertise(Boolean papillaryExpertise) {
            dto.papillaryExpertise = papillaryExpertise;
            return this;
        }

        public ProtocolDTOBuilder withOwnerIntimate(Boolean ownerIntimate) {
            dto.ownerIntimate = ownerIntimate;
            return this;
        }

        public ProtocolDTOBuilder withAuthorizedAlienation(Boolean authorizedAlienation) {
            dto.authorizedAlienation = authorizedAlienation;
            return this;
        }

        public ProtocolDTOBuilder withDebits(Boolean debits) {
            dto.debits = debits;
            return this;
        }

        public ProtocolDTOBuilder withShed(String shed) {
            dto.shed = shed;
            return this;
        }

        public ProtocolDTOBuilder withRow(String row) {
            dto.row = row;
            return this;
        }

        public ProtocolDTOBuilder withColumn(String column) {
            dto.column = column;
            return this;
        }

        public ProtocolDTOBuilder withFloor(String floor) {
            dto.floor = floor;
            return this;
        }

        public ProtocolDTOBuilder withAuthentication(String authentication) {
            dto.authentication = authentication;
            return this;
        }

        public ProtocolDTOBuilder withAmountSeals(Integer amountSeals) {
            dto.amountSeals = amountSeals;
            return this;
        }

        public ProtocolDTOBuilder withArrestOrgan(String arrestOrgan) {
            dto.arrestOrgan = arrestOrgan;
            return this;
        }

        public ProtocolDTOBuilder withAccountableOut(String accountableOut) {
            dto.accountableOut = accountableOut;
            return this;
        }

        public ProtocolDTOBuilder withAccountableIn(String accountableIn) {
            dto.accountableIn = accountableIn;
            return this;
        }

        public ProtocolDTO build() {
            return dto;
        }
    }

    @Override
    public String toString() {
        return "ProtocolDTO [protocol=" + protocol + "]";
    }

}
