package br.com.sw2you.realmeet.service;

import static br.com.sw2you.realmeet.util.DateUtils.*;
import static java.time.LocalTime.*;
import static java.util.Objects.*;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.api.model.UpdateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.repository.AllocationRepository;
import br.com.sw2you.realmeet.domain.repository.RoomRepository;
import br.com.sw2you.realmeet.exception.badRequest.AllocationCannotBeDeleteException;
import br.com.sw2you.realmeet.exception.badRequest.AllocationCannotBeUpdatedException;
import br.com.sw2you.realmeet.exception.notFound.AllocationNotFoundException;
import br.com.sw2you.realmeet.exception.notFound.RoomNotFoundException;
import br.com.sw2you.realmeet.mapper.AllocationMapper;
import br.com.sw2you.realmeet.util.DateUtils;
import br.com.sw2you.realmeet.validator.AllocationValidator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AllocationService {
    private final AllocationRepository allocationRepository;
    private final RoomRepository roomRepository;
    private final AllocationMapper allocationMapper;
    private final AllocationValidator allocationValidator;

    public AllocationService(
        AllocationRepository allocationRepository,
        RoomRepository roomRepository,
        AllocationMapper allocationMapper,
        AllocationValidator allocationValidator
    ) {
        this.allocationRepository = allocationRepository;
        this.roomRepository = roomRepository;
        this.allocationMapper = allocationMapper;
        this.allocationValidator = allocationValidator;
    }

    public AllocationDTO create(CreateAllocationDTO createAllocationDTO) {
        allocationValidator.validate(createAllocationDTO);

        var room = roomRepository
            .findById(createAllocationDTO.getRoomId())
            .orElseThrow(() -> new RoomNotFoundException(createAllocationDTO.getRoomId()));

        var allocation = allocationMapper.fromCreateAllocationDtoToEntity(createAllocationDTO, room);

        allocationRepository.save(allocation);

        return allocationMapper.fromEntityToDto(allocation);
    }

    public void delete(Long id) {
        var allocation = getAllocationOrThrow(id);

        if (isPast(allocation.getEndAt())) {
            throw new AllocationCannotBeDeleteException(id);
        }

        allocationRepository.delete(allocation);
    }

    private Allocation getAllocationOrThrow(Long id) {
        return allocationRepository.findById(id).orElseThrow(() -> new AllocationNotFoundException(id));
    }

    @Transactional
    public void update(Long id, UpdateAllocationDTO updateAllocationDTO) {
        var allocation = getAllocationOrThrow(id);

        if (isPast(allocation.getEndAt())) {
            throw new AllocationCannotBeUpdatedException(id);
        }

        allocationValidator.validate(id, updateAllocationDTO);

        allocationRepository.update(
            id,
            updateAllocationDTO.getSubject(),
            updateAllocationDTO.getStartAt(),
            updateAllocationDTO.getEndAt()
        );
    }

    public List<AllocationDTO> list(String employeeEmail, Long roomId, LocalDate startAt, LocalDate endAt) {
        var allocations = allocationRepository.findAllWithFilters(
            employeeEmail,
            roomId,
            isNull(startAt) ? null : startAt.atTime(MIN).atOffset(DEFAULT_TIMEZONE),
            isNull(endAt) ? null : endAt.atTime(MAX).atOffset(DEFAULT_TIMEZONE)
        );

        return allocations.stream().map(allocationMapper::fromEntityToDto).collect(Collectors.toList());
    }
}
