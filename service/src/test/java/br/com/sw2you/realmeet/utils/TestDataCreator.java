package br.com.sw2you.realmeet.utils;

import static br.com.sw2you.realmeet.domain.entity.Room.Builder;
import static br.com.sw2you.realmeet.domain.entity.Room.newBuilder;
import static br.com.sw2you.realmeet.utils.TestConstants.*;

import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.model.Employee;

public final class TestDataCreator {

    private TestDataCreator() {}

    public static Builder newRoomBuilder() {
        return newBuilder().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static CreateRoomDTO newCreateRoomDTO() {
        return new CreateRoomDTO().name(DEFAULT_ROOM_NAME).seats(DEFAULT_ROOM_SEATS);
    }

    public static Allocation.Builder newAllocationBuilder(Room room) {
        return Allocation
            .newBuilder()
            .subject(DEFAULT_ALLOCATION_SUBJECT)
            .employee(Employee.newBuilder().name(DEFAULT_ALLOCATION_NAME).email(DEFAULT_ALLOCATION_EMAIL).build())
            .startAt(DEFAULT_ALLOCATION_START_AT)
            .endAt(DEFAULT_ALLOCATION_END_AT)
            .room(room);
    }

    public static CreateAllocationDTO newCreateAllocationDTO() {
        return new CreateAllocationDTO()
            .subject(DEFAULT_ALLOCATION_SUBJECT)
            .employeeName(DEFAULT_ALLOCATION_NAME)
            .employeeEmail(DEFAULT_ALLOCATION_EMAIL)
            .startAt(DEFAULT_ALLOCATION_START_AT)
            .endAt(DEFAULT_ALLOCATION_END_AT)
            .roomId(DEFAULT_ALLOCATION_ROOM_ID);
    }

    public static UpdateAllocationDTO newUpdateAllocationDTO() {
        return new UpdateAllocationDTO()
            .subject(DEFAULT_ALLOCATION_SUBJECT)
            .startAt(DEFAULT_ALLOCATION_START_AT)
            .endAt(DEFAULT_ALLOCATION_END_AT);
    }
}
