package br.com.sw2you.realmeet.integration;

import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateRoomDTO;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.*;

import br.com.sw2you.realmeet.api.facade.RoomApi;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.core.BaseIntegrationTest;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.HttpClientErrorException;

class RoomApiIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private RoomApi api;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    protected void setupEach() throws Exception {
        setLocalHostBasePath(api.getApiClient(), "/v1");
    }

    @Test
    void testFindSuccess() {
        var room = newRoomBuilder().build();
        roomRepository.saveAndFlush(room);

        assertNotNull(room.getId());
        assertTrue(room.getActive());

        var dto = api.find(DEFAULT_ROOM_ID);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testFindInactive() {
        var room = newRoomBuilder().active(false).build();
        roomRepository.saveAndFlush(room);

        assertThrows(HttpClientErrorException.NotFound.class, () -> api.find(DEFAULT_ROOM_ID));
    }

    @Test
    void testFindDoesNotExists() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.find(DEFAULT_ROOM_ID));
    }

    @Test
    void testCreateSuccess() {
        var createRoomDTO = newCreateRoomDTO();
        var roomDTO = api.create(createRoomDTO);
        var room = roomRepository.findById(roomDTO.getId()).orElseThrow();

        assertEquals(createRoomDTO.getName(), room.getName());
        assertEquals(createRoomDTO.getSeats(), room.getSeats());
        assertNotNull(room.getId());
    }

    @Test
    void testCreateValidationError() {
        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.create(newCreateRoomDTO().name(null))
        );
    }

    @Test
    void testDeleteSuccess() {
        var roomId = roomRepository.saveAndFlush(newRoomBuilder().build()).getId();
        api.delete(roomId);

        assertFalse(roomRepository.findById(roomId).orElseThrow().getActive());
    }

    @Test
    void testDeleteDoesNotExist() {
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.delete(DEFAULT_ROOM_ID));
    }

    @Test
    void testUpdateSuccess() {
        var room = roomRepository.saveAndFlush(newRoomBuilder().build());
        var updateRoomDto = (UpdateRoomDTO) new UpdateRoomDTO().name(room.getName() + "_").seats(room.getSeats() + 1);

        api.update(room.getId(), updateRoomDto);

        var updateRoom = roomRepository.findById(room.getId()).orElseThrow();
        assertEquals(updateRoomDto.getName(), updateRoom.getName());
        assertEquals(updateRoomDto.getSeats(), updateRoom.getSeats());
    }

    @Test
    void testUpdateDoestNotExists() {
        var updateRoomDto = (UpdateRoomDTO) new UpdateRoomDTO().name("Room Does Not Exist").seats(1);
        assertThrows(HttpClientErrorException.NotFound.class, () -> api.update(DEFAULT_ROOM_ID, updateRoomDto));
    }

    @Test
    void testUpdateValidationError() {
        assertThrows(
            HttpClientErrorException.UnprocessableEntity.class,
            () -> api.update(DEFAULT_ROOM_ID, new UpdateRoomDTO())
        );
    }
}
