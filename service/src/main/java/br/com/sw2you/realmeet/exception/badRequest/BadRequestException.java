package br.com.sw2you.realmeet.exception.badRequest;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
