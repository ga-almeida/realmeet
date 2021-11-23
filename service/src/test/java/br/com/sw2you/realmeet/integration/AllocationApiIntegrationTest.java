package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.util.DateUtils.now;
import static br.com.sw2you.realmeet.utils.TestConstants.*;
import static br.com.sw2you.realmeet.utils.TestDataCreator.*;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.api.facade.AllocationApi;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;
import org.junit.jupiter.api.Test;

class AllocationApiIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private AllocationApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AllocationRepository allocationRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testCreateAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO = api.createAllocation(createAllocationDTO);

        assertNotNull(allocationDTO.getId());
        assertEquals(room.getId(), allocationDTO.getRoomId());
        assertEquals(createAllocationDTO.getSubject(), allocationDTO.getSubject());
        assertEquals(createAllocationDTO.getEmployeeName(), allocationDTO.getEmployeeName());
        assertEquals(createAllocationDTO.getEmployeeEmail(), allocationDTO.getEmployeeEmail());
        assertTrue(createAllocationDTO.getStartAt().isEqual(allocationDTO.getStartAt()));
        assertTrue(createAllocationDTO.getEndAt().isEqual(allocationDTO.getEndAt()));
    }

    @Test
    void testCreateAllocationValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());

        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.createAllocation(createAllocationDTO.subject(null))
        );
    }

    @Test
    void testCreateAllocationWhenRoomNotFound() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.createAllocation(newCreateAllocationDTO()));
    }

    @Test
    void testDeleteAllocationSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(newAllocationBuilder(room).build());

        api.deleteAllocation(allocation.getId());
        assertFalse(allocationRepository.findById(allocation.getId()).isPresent());
    }

    @Test
    void testDeleteAllocationInThePast() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation = allocationRepository.saveAndFlush(
            newAllocationBuilder(room).startAt(now().minusDays(1)).endAt(now().minusDays(1).plusHours(1)).build()
        );

        assertThrows(HttpClientErrorException.BadRequest.class, () -> api.deleteAllocation(allocation.getId()));
    }

    @Test
    void testDeleteAllocationNotFound() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.deleteAllocation(1L));
    }

    @Test
    void testUpdateSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO = api.createAllocation(createAllocationDTO);

        var updateAllocationDTO = newUpdateAllocationDTO()
            .subject(DEFAULT_ALLOCATION_SUBJECT + "teste")
            .startAt(DEFAULT_ALLOCATION_START_AT.plusDays(1))
            .endAt(DEFAULT_ALLOCATION_END_AT.plusDays(1).plusHours(3));

        api.updateAllocation(allocationDTO.getId(), updateAllocationDTO);

        var updateAllocation = allocationRepository.findById(allocationDTO.getId()).orElseThrow();
        assertEquals(updateAllocationDTO.getSubject(), updateAllocation.getSubject());

        assertEquals(
            updateAllocationDTO.getStartAt().truncatedTo(SECONDS),
            updateAllocation.getStartAt().truncatedTo(SECONDS)
        );
        assertEquals(
            updateAllocationDTO.getEndAt().truncatedTo(SECONDS),
            updateAllocation.getEndAt().truncatedTo(SECONDS)
        );
    }

    @Test
    void testUpdateAllocationDoestNotExists() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.updateAllocation(1L, newUpdateAllocationDTO()));
    }

    @Test
    void testUpdateAllocationValidationError() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var createAllocationDTO = newCreateAllocationDTO().roomId(room.getId());
        var allocationDTO = api.createAllocation(createAllocationDTO);

        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.updateAllocation(allocationDTO.getId(), newUpdateAllocationDTO().subject(null))
        );
    }

    @Test
    void filterAllocations() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var allocation1 = allocationRepository.saveAndFlush(newAllocationBuilder(room).subject(DEFAULT_ALLOCATION_SUBJECT + "1").build());
        var allocation2 = allocationRepository.saveAndFlush(newAllocationBuilder(room).subject(DEFAULT_ALLOCATION_SUBJECT + "2").build());
        var allocation3 = allocationRepository.saveAndFlush(newAllocationBuilder(room).subject(DEFAULT_ALLOCATION_SUBJECT + "2").build());

        var allocations = api.listAllocation(null, null, null, null);
        assertEquals(3, allocations.size());
    }
}
