package br.com.patiolegal.dto;

import java.lang.reflect.Field;

import org.springframework.util.ReflectionUtils;

public class SealRequestDTOStub extends SealRequestDTO {

    public SealRequestDTOStub(String protocol, Integer amount, String reason) {
        Field fieldProtocol = ReflectionUtils.findField(SealRequestDTO.class, "protocol");
        Field fieldAmount = ReflectionUtils.findField(SealRequestDTO.class, "amount");
        Field fieldReason = ReflectionUtils.findField(SealRequestDTO.class, "reason");

        ReflectionUtils.makeAccessible(fieldProtocol);
        ReflectionUtils.makeAccessible(fieldAmount);
        ReflectionUtils.makeAccessible(fieldReason);

        ReflectionUtils.setField(fieldProtocol, this, protocol);
        ReflectionUtils.setField(fieldAmount, this, amount);
        ReflectionUtils.setField(fieldReason, this, reason);
    }

    public SealRequestDTOStub() {
        this("AAAAA", 5, "XXX");
    }

}
