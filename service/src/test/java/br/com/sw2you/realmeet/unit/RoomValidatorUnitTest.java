package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_NAME;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateRoomDTO;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilder;
import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import br.com.sw2you.realmeet.validator.RoomValidator;
import br.com.sw2you.realmeet.validator.ValidationError;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class RoomValidatorUnitTest extends BaseUnitTest {
    private RoomValidator victim;

    @Mock
    private RoomRepository roomRepository;

    @BeforeEach
    void setupEach() {
        victim = new RoomValidator(roomRepository);
    }

    @Test
    void testValidateWhenRoomIsValid() {
        victim.validate(newCreateRoomDTO());
    }

    @Test
    void testValidateWhenRoomNameIsMissing() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().name(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(new ValidationError(ROOM_NAME, ROOM_NAME + MISSING), exception.getValidationErrors().getError(0));
    }

    @Test
    void testValidateWhenRoomNameIsExceedsMaxLength() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () ->
                victim.validate(
                    newCreateRoomDTO().name(StringUtils.rightPad("Max", ROOM_NAME_MAX_LENGTH + 1, "Length"))
                )
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + EXCEEDS_MAX_LENGTH),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreMissing() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().seats(null))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + MISSING),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreLessThenMinValue() {
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDTO().seats(0)));
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + BELOW_MIN_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWhenRoomSeatsAreLessThenMaxValue() {
        var exception = assertThrows(
            InvalidRequestException.class,
            () -> victim.validate(newCreateRoomDTO().seats(21))
        );
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_SEATS, ROOM_SEATS + EXCEEDS_MAX_VALUE),
            exception.getValidationErrors().getError(0)
        );
    }

    @Test
    void testValidateWheRoomNameDuplicate() {
        when(roomRepository.findByNameAndActive(DEFAULT_ROOM_NAME, true))
            .thenReturn(Optional.of(newRoomBuilder().build()));
        var exception = assertThrows(InvalidRequestException.class, () -> victim.validate(newCreateRoomDTO()));
        assertEquals(1, exception.getValidationErrors().getNumberOfErrors());
        assertEquals(
            new ValidationError(ROOM_NAME, ROOM_NAME + DUPLICATE),
            exception.getValidationErrors().getError(0)
        );
    }
}
