package br.com.sw2you.realmeet.unit;

import static br.com.sw2you.realmeet.utils.MapperUtils.roomMapper;
import static br.com.sw2you.realmeet.utils.TestConstants.DEFAULT_ROOM_ID;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newCreateRoomDTO;
import static br.com.sw2you.realmeet.utils.TestDataCreator.newRoomBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.sw2you.realmeet.core.BaseUnitTest;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomMapperUnitTest extends BaseUnitTest {
    private RoomMapper victim;

    /**
     * Essa anotação vai fazer com que esse método seja executado
     * toda vez antes de cada teste.
     */
    @BeforeEach
    void setupEach() {
        victim = roomMapper();
    }

    /**
     * Anotação utilizada para atribuir a função como um teste
     */
    @Test
    void testFromEntityToDto() {
        var room = newRoomBuilder().id(DEFAULT_ROOM_ID).build();
        var dto = victim.fromEntityToDto(room);

        assertEquals(room.getId(), dto.getId());
        assertEquals(room.getName(), dto.getName());
        assertEquals(room.getSeats(), dto.getSeats());
    }

    @Test
    void testCreateRoomDtoToEntity() {
        var roomDTO = newCreateRoomDTO();
        var room = victim.fromCreateRoomDtoToEntity(roomDTO);

        assertEquals(roomDTO.getName(), room.getName());
        assertEquals(roomDTO.getSeats(), room.getSeats());
    }
}
