package br.com.sw2you.realmeet.exception;

public class RoomNotFoundException extends RuntimeException {

    public RoomNotFoundException(Long id) {
        super("Room not found by: " + id);
    }
}
