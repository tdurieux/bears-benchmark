package br.com.patiolegal.utils;

import java.security.MessageDigest;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.patiolegal.exception.PasswordEncryptException;

@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    private static final Logger LOG = LogManager.getLogger(CustomPasswordEncoder.class);
    private static final String ALGORITHM = "SHA-256";
    private static final String CHARSET = "UTF-8";

    @Override
    public String encode(CharSequence rawPassword) {
        StringBuilder hexPass = new StringBuilder();
        try {
            String password = String.valueOf(rawPassword);
            MessageDigest algorithm = MessageDigest.getInstance(ALGORITHM);
            byte[] passDigest = algorithm.digest(password.getBytes(CHARSET));

            for (byte b : passDigest) {
                hexPass.append(String.format("%02X", 0xFF & b));
            }

            return String.valueOf(hexPass);
        } catch (Exception e) {
            LOG.error("Erro ao encriptar a senha", e);
            throw new PasswordEncryptException();
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String password = encode(rawPassword);
        return StringUtils.equals(password, encodedPassword);
    }

}
