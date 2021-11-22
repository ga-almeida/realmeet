package br.com.sw2you.realmeet.exception.badParameter;

public class AllocationCannotBeDeleteException extends BadParameterException {
    public static final String MESSAGE_ERROR = "Cannot delete a past allocation by ID: %s";

    public AllocationCannotBeDeleteException(Long id) {
        super(MESSAGE_ERROR + id);
    }
}
