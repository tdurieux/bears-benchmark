package br.com.patiolegal.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "protocolRecord")
public class ProtocolRecord {

    @Id
    private String id;
    private LocalDateTime date = LocalDateTime.now();
    private String username;
    private Protocol protocol;
    private String authentication;
    private Binary file;

    public void generateAuthentication() {
        byte[] bytes = date.toString().getBytes();
        UUID uuid = UUID.nameUUIDFromBytes(bytes);
        authentication = StringUtils.upperCase(uuid.toString());
    }

    public void setFile(Binary file) {
        this.file = file;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getAuthentication() {
        return authentication;
    }

    public Binary getFile() {
        return file;
    }
}
