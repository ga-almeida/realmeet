package br.com.sw2you.realmeet.service;

import static br.com.sw2you.realmeet.domain.entity.Room.*;
import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    private RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public RoomDTO find(Long id) {
        requireNonNull(id);
        Room room = roomRepository.findByIdAndActive(id, true).orElseThrow(() -> new RoomNotFoundException(id));

        return roomMapper.fromEntityToDto(room);
    }

    public RoomDTO create(CreateRoomDTO createRoomDTO) {
        var room = roomMapper.fromCreateRoomDtoToEntity(createRoomDTO);
        roomRepository.saveAndFlush(room);

        return roomMapper.fromEntityToDto(room);
    }
}
