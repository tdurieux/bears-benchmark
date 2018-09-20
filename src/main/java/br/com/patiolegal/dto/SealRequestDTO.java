package br.com.patiolegal.dto;

import javax.validation.constraints.NotNull;

public class SealRequestDTO {

    @NotNull
    private String protocol;
    @NotNull
    private Integer amount;
    @NotNull
    private String reason;

    public String getProtocol() {
        return protocol;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getReason() {
        return reason;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SealRequestDTO [protocol=");
        builder.append(protocol);
        builder.append(", amount=");
        builder.append(amount);
        builder.append(", reason=");
        builder.append(reason);
        builder.append("]");
        return builder.toString();
    }

}
