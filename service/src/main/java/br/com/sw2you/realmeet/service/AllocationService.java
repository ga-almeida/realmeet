package br.com.sw2you.realmeet.service;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import org.springframework.stereotype.Service;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final RoomRepository roomRepository;
    private final AllocationMapper allocationMapper;

    public AllocationService(
        AllocationRepository allocationRepository,
        RoomRepository roomRepository,
        AllocationMapper allocationMapper
    ) {
        this.allocationRepository = allocationRepository;
        this.roomRepository = roomRepository;
        this.allocationMapper = allocationMapper;
    }

    public AllocationDTO create(CreateAllocationDTO createAllocationDTO) {
        var room = roomRepository.findById(createAllocationDTO.getRoomId())
            .orElseThrow(() -> new RoomNotFoundException(createAllocationDTO.getRoomId()));

        var allocation = allocationMapper.fromCreateAllocationDtoToEntity(createAllocationDTO, room);

        allocationRepository.save(allocation);

        return allocationMapper.fromEntityToDto(allocation);
    }
}
