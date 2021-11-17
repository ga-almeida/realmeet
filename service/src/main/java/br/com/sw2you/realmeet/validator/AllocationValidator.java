package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2you.realmeet.validator.ValidatorUtils.*;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import java.time.OffsetDateTime;
import org.springframework.stereotype.Component;

@Component
public class AllocationValidator {
    private final AllocationRepository allocationRepository;

    public AllocationValidator(AllocationRepository allocationRepository) {
        this.allocationRepository = allocationRepository;
    }

    public void validate(CreateAllocationDTO createAllocationDTO) {
        var validationErrors = new ValidationErrors();

        validateSubject(createAllocationDTO.getSubject(), validationErrors);
        validateEmployeeName(createAllocationDTO.getEmployeeName(), validationErrors);
        validateEmployeeEmail(createAllocationDTO.getEmployeeEmail(), validationErrors);
        validateDates(createAllocationDTO.getStartAt(), createAllocationDTO.getEndAt(), validationErrors);

        throwOnError(validationErrors);
    }

    private void validateSubject(String subject, ValidationErrors validationErrors) {
        validateRequired(subject, ALLOCATION_SUBJECT, validationErrors);
        validateMaxLength(subject, ALLOCATION_SUBJECT, ALLOCATION_SUBJECT_MAX_LENGTH, validationErrors);
    }

    private void validateEmployeeName(String employeeName, ValidationErrors validationErrors) {
        validateRequired(employeeName, ALLOCATION_EMPLOYEE_NAME, validationErrors);
        validateMaxLength(employeeName, ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH, validationErrors);
    }

    private void validateEmployeeEmail(String employeeEmail, ValidationErrors validationErrors) {
        validateRequired(employeeEmail, ALLOCATION_EMPLOYEE_EMAIL, validationErrors);
        validateMaxLength(employeeEmail, ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH, validationErrors);
    }

    private void validateDates(OffsetDateTime startAt, OffsetDateTime endAt, ValidationErrors validationErrors) {
        validateRequired(startAt, ALLOCATION_START_AT, validationErrors);
        validateDateInTheFuture(startAt, ALLOCATION_START_AT, validationErrors);
        validateRequired(endAt, ALLOCATION_END_AT, validationErrors);
        validateDateInTheFuture(endAt, ALLOCATION_END_AT, validationErrors);
        validateDateOrdering(startAt, endAt, ALLOCATION_DATES, validationErrors);
    }
}
