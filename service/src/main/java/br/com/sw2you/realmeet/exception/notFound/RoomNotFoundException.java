package br.com.sw2you.realmeet.exception.notFound;

public class RoomNotFoundException extends NotFoundException {
    public static final String MESSAGE_ERROR = "Room not found by ID: %s";

    public RoomNotFoundException(Long id) {
        super(String.format(MESSAGE_ERROR, id));
    }
}
