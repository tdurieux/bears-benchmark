package br.com.patiolegal.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "seal")
public class Seal {

    @Id
    private String id;
    private LocalDateTime dateTime = LocalDateTime.now();
    private String username;
    private Integer amount;
    private String reason;
    private Binary file;
    private String authentication;

    public void generateAuthentication() {
        byte[] bytes = dateTime.toString().getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(bytes);
        authentication = StringUtils.upperCase(uuid.toString());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Binary getFile() {
        return file;
    }

    public void setFile(Binary file) {
        this.file = file;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getAuthentication() {
        return authentication;
    }

}
