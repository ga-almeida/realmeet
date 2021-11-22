package br.com.sw2you.realmeet.util;

import static org.springframework.http.HttpStatus.*;

import br.com.sw2you.realmeet.api.model.ResponseError;
import org.springframework.http.ResponseEntity;

public final class ResponseEntityUtils {

    private ResponseEntityUtils() {}

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.status(OK).body(body);
    }

    public static <T> ResponseEntity<T> created(T body) {
        return ResponseEntity.status(CREATED).body(body);
    }

    public static <T> ResponseEntity<T> notFound(T responseError) {
        return ResponseEntity.status(NOT_FOUND).body(responseError);
    }

    public static <T> ResponseEntity<T> noContent(Void aVoid) {
        return ResponseEntity.status(NO_CONTENT).build();
    }
}
