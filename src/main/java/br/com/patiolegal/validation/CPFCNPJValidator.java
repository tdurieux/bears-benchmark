package br.com.patiolegal.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang.StringUtils;

import br.com.caelum.stella.validation.CNPJValidator;
import br.com.caelum.stella.validation.CPFValidator;

public class CPFCNPJValidator implements ConstraintValidator<CPFCNPJ, String> {

    @Override
    public void initialize(CPFCNPJ cpfcnpj) {
        // do nothing
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.length(value) != 11 && StringUtils.length(value) != 14) {
            return false;
        } else if (StringUtils.length(value) == 11) {
            validateCPF(value);
        } else {
            validateCNPJ(value);
        }
        return true;
    }

    private void validateCNPJ(String value) {
        CNPJValidator validator = new CNPJValidator();
        validator.assertValid(value);
    }

    private void validateCPF(String value) {
        CPFValidator validator = new CPFValidator();
        validator.assertValid(value);
    }

}