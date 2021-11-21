package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateAllocationDTO;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class AllocationValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(newCreateAllocationDTO());
    }

    @Test
    void testValidateWhenAllocationSubjectIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDTO().subject(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenAllocationSubjectExceedsLength() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDTO().subject(StringUtils.rightPad("Max", ALLOCATION_SUBJECT_MAX_LENGTH + 1, "Subject")))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenAllocationEmployeeNameIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDTO().employeeName(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenAllocationEmployeeNameExceedsLength() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDTO().employeeName(StringUtils.rightPad("Max", ALLOCATION_EMPLOYEE_NAME_MAX_LENGTH + 1, "EmployeeName")))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_NAME, ALLOCATION_EMPLOYEE_NAME + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenAllocationEmployeeEmailIsMissing() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDTO().employeeEmail(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenAllocationEmployeeEmailExceedsLength() {
        var exception = assertThrows(
                InvalidRequestException.class,
                () -> victim.validate(newCreateAllocationDTO().employeeEmail(StringUtils.rightPad("Max", ALLOCATION_EMPLOYEE_EMAIL_MAX_LENGTH + 1, "EmployeeEmail")))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ALLOCATION_EMPLOYEE_EMAIL, ALLOCATION_EMPLOYEE_EMAIL + EXCEEDS_MAX_LENGTH), exception.getValidationErrors().getError(0));
    }
}
