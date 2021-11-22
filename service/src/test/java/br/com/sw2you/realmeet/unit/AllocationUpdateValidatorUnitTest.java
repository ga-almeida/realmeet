package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.util.DateUtils.now;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ALLOCATION_ID;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateAllocationDTO;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newUpdateAllocationDTO;
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

class AllocationUpdateValidatorUnitTest extends BaseUnitTest {
    private AllocationValidator victim;

    @Mock
    private AllocationRepository allocationRepository;

    @BeforeEach
    void setupEach() {
        victim = new AllocationValidator(allocationRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO());
    }

    @Test
    void testValidateWhenAllocationSubjectIsMissing() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().subject(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationSubjectExceedsLength() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO()
                        .subject(StringUtils.rightPad("Max", ALLOCATION_SUBJECT_MAX_LENGTH + 1, "Subject"))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_SUBJECT, ALLOCATION_SUBJECT + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationStartAtIsMissing() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().startAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationStartAtInTheFuture() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO().startAt(now().minusHours(2)).endAt(now().plusHours(1))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_START_AT, ALLOCATION_START_AT + IN_THE_PAST),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationEndAtIsMissing() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().endAt(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationEndAtInTheFuture() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    DEFAULT_ALLOCATION_ID,
                    newUpdateAllocationDTO().startAt(now().plusHours(1)).endAt(now().minusHours(1))
                )
        );
        assertEquals(
            new ValidationError(ALLOCATION_END_AT, ALLOCATION_END_AT + IN_THE_PAST),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationDateOrdering() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().endAt(now()))
        );
        assertEquals(
            new ValidationError(ALLOCATION_DATES, ALLOCATION_DATES + INCONSISTENT),
            exception.getValidationErrors().getError(1)
        );
    }

    @Test
    void testValidateWhenAllocationDuration() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(DEFAULT_ALLOCATION_ID, newUpdateAllocationDTO().endAt(now().plusDays(2)))
        );
        assertEquals(
            new ValidationError(ALLOCATION_DATES, ALLOCATION_DATES + EXCEEDS_DURATION),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenAllocationIdNull() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(null, newUpdateAllocationDTO())
        );
        assertEquals(
            new ValidationError(ALLOCATION_ID, ALLOCATION_ID + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }
}
