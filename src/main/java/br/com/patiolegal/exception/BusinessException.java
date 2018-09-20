package br.com.patiolegal.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String fieldName;
    private final String message;

    public BusinessException(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;

    }

    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
