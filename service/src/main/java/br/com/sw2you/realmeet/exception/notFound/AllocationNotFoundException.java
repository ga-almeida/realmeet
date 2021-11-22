package br.com.sw2you.realmeet.exception.notFound;

public class AllocationNotFoundException extends NotFoundException {
    private static final String MESSAGE_ERROR = "Allocation not found by ID: ";

    public AllocationNotFoundException(Long id) {
        super(MESSAGE_ERROR + id);
    }
}
