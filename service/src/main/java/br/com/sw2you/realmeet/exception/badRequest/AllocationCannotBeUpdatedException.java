package br.com.sw2you.realmeet.exception.badRequest;

import static java.lang.String.format;

public class AllocationCannotBeUpdatedException extends BadRequestException {
    public static final String MESSAGE_ERROR = "Cannot updated a past allocation by ID: %s";

    public AllocationCannotBeUpdatedException(Long id) {
        super(format(MESSAGE_ERROR, id));
    }
}
