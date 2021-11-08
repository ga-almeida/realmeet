package br.com.sw2you.realmeet.unit;

import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.utils.MapperUtils;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

// Utilizar essa anotação para utilizar MOCKS
@ExtendWith(MockitoExtension.class)
class RoomMapperUnit {

    private RoomMapper victim;

    /**
     * Essa anotação vai fazer com que esse método seja executado
     * toda vez antes de cada teste.
     */
    @BeforeEach
    void setupEach() {
        victim = MapperUtils.roomMapper();
    }

    /**
     * Anotação utilizada para atribuir a função como um teste
     */
    @Test
    void testFromEntityToDto() {

    }
}
