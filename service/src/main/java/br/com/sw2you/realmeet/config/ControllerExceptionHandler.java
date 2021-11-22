package br.com.sw2you.realmeet.config;

import static br.com.sw2you.realmeet.util.ResponseEntityUtils.notFound;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import br.com.sw2you.realmeet.api.model.ResponseError;
import br.com.sw2you.realmeet.api.model.ResponseErrorItems;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.exception.notFound.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseError> handleNotFoundException(NotFoundException exception) {
        return notFound(new ResponseError().errorCode(NOT_FOUND.getReasonPhrase()).message(exception.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseBody
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public List<ResponseErrorItems> handleInvalidRequestException(InvalidRequestException exception) {
        return exception
            .getValidationErrors()
            .stream()
            .map(e -> new ResponseErrorItems().field(e.getField()).errorCode(e.getErrorCode()))
            .collect(Collectors.toList());
    }
}
