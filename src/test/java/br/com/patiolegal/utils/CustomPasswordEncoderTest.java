package br.com.patiolegal.utils;

import org.junit.Assert;
import org.junit.Test;

public class CustomPasswordEncoderTest {

    //https://medium.com/@pjbgf/title-testing-code-ocd-and-the-aaa-pattern-df453975ab80
    @Test
    public void shouldEncryptPassword() {
        // Arrange
        String password = "mypassword";
        String encryptedPassword = "89E01536AC207279409D4DE1E5253E01F4A1769E696DB0D6062CA9B8F56767C8";

        // Act
        CustomPasswordEncoder encryption = new CustomPasswordEncoder();
        String actual = encryption.encode(password);

        // Assert
        Assert.assertEquals(encryptedPassword, actual);
    }

    @Test
    public void shouldValidatePassword() {
        // Arrange
        CharSequence rawPassword = "mypassword";
        String encryptedPassword = "89E01536AC207279409D4DE1E5253E01F4A1769E696DB0D6062CA9B8F56767C8";

        // Act
        CustomPasswordEncoder encryption = new CustomPasswordEncoder();
        Boolean actual = encryption.matches(rawPassword, encryptedPassword);

        // Assert
        Assert.assertTrue(actual);
    }
}
