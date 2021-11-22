package br.com.sw2you.realmeet.exception.badRequest;

import static java.lang.String.format;

public class AllocationCannotBeDeleteException extends BadRequestException {
    public static final String MESSAGE_ERROR = "Cannot delete a past allocation by ID: %s";

    public AllocationCannotBeDeleteException(Long id) {
        super(format(MESSAGE_ERROR, id));
    }
}
