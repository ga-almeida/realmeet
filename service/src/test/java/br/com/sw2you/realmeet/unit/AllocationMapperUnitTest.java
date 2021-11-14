package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.MapperUtils.allocationMapper;
import static br.com.sw2you.realmeet.utils.MapperUtils.roomMapper;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ALLOCATION_ID;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.utils.TestDataCreator.*;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateAllocationDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AllocationMapperUnitTest extends BaseUnitTest {
    private AllocationMapper victim;

    @BeforeEach
    void setupEach() {
        victim = allocationMapper();
    }

    @Test
    void testFromEntityToDto() {
        var room = newRoomBuilder().id(DEFAULT_ROOM_ID).build();
        var allocation = newAllocationBuilder(room).id(DEFAULT_ALLOCATION_ID).build();
        var allocationDTO = victim.fromEntityToDto(allocation);

        assertEquals(allocationDTO.getId(), allocation.getId());
        assertEquals(allocationDTO.getSubject(), allocation.getSubject());
        assertEquals(allocationDTO.getEmployeeName(), allocation.getEmployee().getName());
        assertEquals(allocationDTO.getEmployeeEmail(), allocation.getEmployee().getEmail());
        assertEquals(allocationDTO.getStartAt(), allocation.getStartAt());
        assertEquals(allocationDTO.getEndAt(), allocation.getEndAt());
        assertEquals(allocationDTO.getRoomId(), allocation.getRoom().getId());
    }

    @Test
    void testCreateAllocationDtoToEntity() {
        var createAllocationDTO = newCreateAllocationDTO();
        var allocation = victim.fromCreateAllocationDtoToEntity(createAllocationDTO, newRoomBuilder().build());

        assertEquals(createAllocationDTO.getSubject(), allocation.getSubject());
        assertEquals(createAllocationDTO.getEmployeeName(), allocation.getEmployee().getName());
        assertEquals(createAllocationDTO.getEmployeeEmail(), allocation.getEmployee().getEmail());
        assertEquals(createAllocationDTO.getStartAt(), allocation.getStartAt());
        assertEquals(createAllocationDTO.getEndAt(), allocation.getEndAt());
        assertNull(allocation.getRoom().getId());
    }
}
