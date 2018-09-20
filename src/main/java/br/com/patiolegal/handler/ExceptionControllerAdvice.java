package br.com.patiolegal.handler;

import java.util.List;

import javax.validation.ValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import br.com.patiolegal.dto.ErrorDTO;
import br.com.patiolegal.exception.AccessDeniedException;
import br.com.patiolegal.exception.BusinessException;

@ControllerAdvice
@RequestMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ExceptionControllerAdvice {

    private static final String MENSAGEM_ERRO = "Ocorreu o seguinte erro: ";
    private static final Logger LOG = LogManager.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDTO> notFoundException(final AccessDeniedException e) {
        LOG.error(MENSAGEM_ERRO + e);
        ErrorDTO error = new ErrorDTO("Access Denied");
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDTO> notFoundException(final BusinessException e) {
        LOG.error(MENSAGEM_ERRO + e);
        ErrorDTO error = new ErrorDTO(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMediaTypeNotAcceptableException(
            final HttpMediaTypeNotAcceptableException e) {
        LOG.error(MENSAGEM_ERRO + e);
        ErrorDTO error = new ErrorDTO(e.getMessage());
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<>(error, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDTO> handleHttpMediaTypeNotAcceptableException(final HttpMessageNotReadableException e) {
        LOG.error(MENSAGEM_ERRO + e);
        InvalidFormatException ex = (InvalidFormatException) e.getCause();
        List<Reference> path = ex.getPath();
        Reference reference = path.stream().findAny().orElseThrow(() -> new ValidationException("Valor inválido"));
        ErrorDTO error = new ErrorDTO("Valor inválido para o fieldName " + reference.getFieldName());
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorDTO> notFoundException(final InvalidFormatException e) {
        LOG.error(MENSAGEM_ERRO + e);
        ErrorDTO error = new ErrorDTO(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDTO> validation(final ValidationException e) {
        LOG.error(MENSAGEM_ERRO + e);
        ErrorDTO error = new ErrorDTO("CPF e/ou CNPJ inválidos");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> notFoundException(final Exception e) {
        LOG.error(MENSAGEM_ERRO + e);
        ErrorDTO error = new ErrorDTO("Não foi possível completar sua requisição.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}