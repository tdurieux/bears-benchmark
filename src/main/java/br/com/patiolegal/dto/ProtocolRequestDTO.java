package br.com.patiolegal.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.br.CPF;

import br.com.patiolegal.domain.ChassisState;
import br.com.patiolegal.domain.EngineState;
import br.com.patiolegal.validation.CPFCNPJ;

public class ProtocolRequestDTO {

    private String part;
    private String protocol;
    private LocalDate date;
    private LocalDateTime dateTimeIn;
    private LocalDateTime dateTimeOut;
    private String policeInvestigation;
    private String eventBulletin;
    @NotNull
    @CPF(message = "CPF inválido")
    private String taxIdentifier;
    @NotNull
    private String name;
    private String theyRenamed;
    private String ownerName;
    @CPFCNPJ(message = "CPF/CNPJ inválido")
    private String ownerTaxIdentifier;
    private String brand;
    private String model;
    private String category;
    private String color;
    private String fuel;
    private Integer factoryYear;
    private Integer modelYear;
    private String sportingPlate;
    private String originalPlate;
    private String originCapture;
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
    private String board;
    private Integer amountSeals;
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

    public String getTaxIdentifier() {
        return taxIdentifier;
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

    public Integer getFactoryYear() {
        return factoryYear;
    }

    public Integer getModelYear() {
        return modelYear;
    }

    public String getSportingPlate() {
        return sportingPlate;
    }

    public String getOriginalPlate() {
        return originalPlate;
    }

    public String getOriginCapture() {
        return originCapture;
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

    public String getBoard() {
        return board;
    }

    public Integer getAmountSeals() {
        return amountSeals;
    }

    public String getAccountableOut() {
        return accountableOut;
    }

    public String getAccountableIn() {
        return accountableIn;
    }

    @Override
    public String toString() {
        return "ProtocolRequestDTO [part=" + part + ", protocol=" + protocol + ", date=" + date + ", dateTimeIn="
                + dateTimeIn + ", dateTimeOut=" + dateTimeOut + ", policeInvestigation=" + policeInvestigation
                + ", eventBulletin=" + eventBulletin + ", taxIdentifier=" + taxIdentifier + ", name=" + name
                + ", theyRenamed=" + theyRenamed + ", ownerName=" + ownerName + ", ownerTaxIdentifier="
                + ownerTaxIdentifier + ", brand=" + brand + ", model=" + model + ", category=" + category + ", color="
                + color + ", fuel=" + fuel + ", factoryYear=" + factoryYear + ", modelYear=" + modelYear
                + ", sportingPlate=" + sportingPlate + ", originalPlate=" + originalPlate + ", originCapture="
                + originCapture + ", chassisState=" + chassisState + ", chassis=" + chassis + ", motorState="
                + motorState + ", motor=" + motor + ", insured=" + insured + ", financed=" + financed + ", stolen="
                + stolen + ", drugTrafficking=" + drugTrafficking + ", moneyLaundry=" + moneyLaundry + ", perquisite="
                + perquisite + ", papillaryExpertise=" + papillaryExpertise + ", ownerIntimate=" + ownerIntimate
                + ", authorizedAlienation=" + authorizedAlienation + ", debits=" + debits + ", shed=" + shed + ", row="
                + row + ", column=" + column + ", floor=" + floor + ", board=" + board + ", amountSeals=" + amountSeals
                + ", accountableOut=" + accountableOut + ", accountableIn=" + accountableIn + "]";
    }

}
