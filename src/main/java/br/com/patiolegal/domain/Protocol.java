package br.com.patiolegal.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.patiolegal.generator.ProtocolGenerator;

@Document(collection = "protocol")
public class Protocol {

    @Id
    private String id;
    private String protocol;
    private String part;
    private LocalDate date;
    private LocalDateTime dateTimeIn = LocalDateTime.now();
    private String policeInvestigation;
    private String eventBulletin;
    private String taxIdentifier;
    private String name;
    private String authentication;
    @DBRef
    private List<Seal> seals = new ArrayList<>();
    private Entrance entrance;
    private Exit exit;
    private Part arrestOrgan;
    private LocalDateTime modificationDate = LocalDateTime.now();
    private Binary file;
    private String accountableOut;
    private String accountableIn;

    public void addSeal(Seal seal) {
        seals.add(seal);
    }

    public String getProtocol() {
        return protocol;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getDateTimeIn() {
        return dateTimeIn;
    }

    public String getPoliceInvestigation() {
        return policeInvestigation;
    }

    public void setPoliceInvestigation(String policeInvestigation) {
        this.policeInvestigation = policeInvestigation;
    }

    public String getEventBulletin() {
        return eventBulletin;
    }

    public void setEventBulletin(String eventBulletin) {
        this.eventBulletin = eventBulletin;
    }

    public String getTaxId() {
        return taxIdentifier;
    }

    public void setTaxIdentifier(String taxIdentifier) {
        this.taxIdentifier = taxIdentifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthentication() {
        return authentication;
    }

    public List<Seal> getSeals() {
        return seals;
    }

    public void setSeals(List<Seal> seals) {
        this.seals = seals;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public void setEntrance(Entrance entrance) {
        this.entrance = entrance;
    }

    public Exit getExit() {
        return exit;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    public Part getArrestOrgan() {
        return arrestOrgan;
    }

    public void setArrestOrgan(Part arrestOrgan) {
        this.arrestOrgan = arrestOrgan;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getModificationDate() {
        return modificationDate;
    }

    public Binary getFile() {
        return file;
    }

    public void setFile(Binary file) {
        this.file = file;
    }

    public String getAccountableOut() {
        return accountableOut;
    }

    public void setAccountableOut(String accountableOut) {
        this.accountableOut = accountableOut;
    }

    public String getAccountableIn() {
        return accountableIn;
    }

    public void setAccountableIn(String accountableIn) {
        this.accountableIn = accountableIn;
    }

    public void generateProtocol() {
        ProtocolGenerator generator = new ProtocolGenerator();
        protocol = generator.generateProtocolNumber(entrance);
    }

    public void generateAuthentication() {
        byte[] bytes = protocol.getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(bytes);
        authentication = StringUtils.upperCase(uuid.toString());
    }

    public Integer getAmountSeals() {
        return getSeals().stream().mapToInt(seal -> seal == null ? 0 : seal.getAmount()).sum();
    }

}