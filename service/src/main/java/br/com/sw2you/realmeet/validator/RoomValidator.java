package br.com.sw2you.realmeet.validator;

import static br.com.sw2you.realmeet.validator.ValidatorConstants.*;
import static br.com.sw2you.realmeet.validator.ValidatorUtils.*;
import static java.util.Objects.isNull;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.InvalidRequestException;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.apache.commons.lang3.ObjectUtils;

@Component
public class RoomValidator {
    private final RoomRepository roomRepository;

    public RoomValidator(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public void validate(CreateRoomDTO createRoomDTO) {
        var validationErrors = new ValidationErrors();

        validateName(null, createRoomDTO.getName(), validationErrors);
        validateSeats(createRoomDTO.getSeats(), validationErrors);

        throwOnError(validationErrors);
    }

    public void validate(Long id, UpdateRoomDTO updateRoomDTO) {
        var validationErrors = new ValidationErrors();

        validateName(id, updateRoomDTO.getName(), validationErrors);
        validateSeats(updateRoomDTO.getSeats(), validationErrors);

        throwOnError(validationErrors);
    }

    private void validateSeats(Integer seats, ValidationErrors validationErrors) {
        validateRequired(seats, ROOM_SEATS, validationErrors);
        validateMinValue(seats, ROOM_SEATS, ROOM_SEATS_MIN_VALUE, validationErrors);
        validateMaxValue(seats, ROOM_SEATS, ROOM_SEATS_MAX_VALUE, validationErrors);
    }

    private void validateName(Long id, String name, ValidationErrors validationErrors) {
        validateRequired(name, ROOM_NAME, validationErrors);
        validateMaxLength(name, ROOM_NAME, ROOM_NAME_MAX_LENGTH, validationErrors);
        validateNameDuplicate(id, name, validationErrors);
    }

    private void validateNameDuplicate(Long roomIdToExclude, String name, ValidationErrors validationErrors) {
        roomRepository
            .findByNameAndActive(name, true)
            .ifPresent(room -> {
                if (isNull(roomIdToExclude)) {
                    validationErrors.add(ROOM_NAME, ROOM_NAME + DUPLICATE);
                } else if (!Objects.equals(room.getId(), roomIdToExclude)) {
                    validationErrors.add(ROOM_NAME, ROOM_NAME + DUPLICATE);
                }
            });
    }
}
