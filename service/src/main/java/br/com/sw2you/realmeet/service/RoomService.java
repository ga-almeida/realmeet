package br.com.sw2you.realmeet.service;

import static java.util.Objects.requireNonNull;

import br.com.sw2you.realmeet.api.model.CreateRoomDTO;
import br.com.sw2you.realmeet.api.model.RoomDTO;
import br.com.sw2you.realmeet.api.model.UpdateRoomDTO;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.notFound.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.RoomMapper;
import br.com.sw2you.realmeet.validator.RoomValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomValidator roomValidator;
    private final RoomMapper roomMapper;

    public RoomService(RoomRepository roomRepository, RoomValidator roomValidator, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomValidator = roomValidator;
        this.roomMapper = roomMapper;
    }

    public RoomDTO find(Long id) {
        Room room = getActiveRoomOrThrow(id);
        return roomMapper.fromEntityToDto(room);
    }

    public RoomDTO create(CreateRoomDTO createRoomDTO) {
        roomValidator.validate(createRoomDTO);
        var room = roomMapper.fromCreateRoomDtoToEntity(createRoomDTO);
        roomRepository.saveAndFlush(room);

        return roomMapper.fromEntityToDto(room);
    }

    @Transactional
    public void delete(Long id) {
        getActiveRoomOrThrow(id);
        roomRepository.deactivate(id);
    }

    private Room getActiveRoomOrThrow(Long id) {
        requireNonNull(id);
        Room room = roomRepository.findByIdAndActive(id, true).orElseThrow(() -> new RoomNotFoundException(id));
        return room;
    }

    @Transactional
    public void update(Long id, UpdateRoomDTO updateRoomDTO) {
        getActiveRoomOrThrow(id);
        roomValidator.validate(id, updateRoomDTO);
        roomRepository.update(id, updateRoomDTO.getName(), updateRoomDTO.getSeats());
    }
}
