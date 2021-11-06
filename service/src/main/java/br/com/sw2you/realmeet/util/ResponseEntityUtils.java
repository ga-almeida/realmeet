package br.com.sw2you.realmeet.util;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;

public final class ResponseEntityUtils {

    private ResponseEntityUtils() {}

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.status(OK).body(body);
    }
}
