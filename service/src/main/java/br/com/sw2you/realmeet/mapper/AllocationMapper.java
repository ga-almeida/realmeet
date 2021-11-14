package br.com.sw2you.realmeet.mapper;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import br.com.sw2you.realmeet.domain.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class AllocationMapper {

    @Mapping(source = "room.id", target = "roomId")
    @Mapping(source = "employee.name", target = "employeeName")
    @Mapping(source = "employee.email", target = "employeeEmail")
    public abstract AllocationDTO fromEntityToDto(Allocation allocation);

    @Mapping(source = "room", target = "room")
    @Mapping(source = "createAllocationDTO.employeeName", target = "employee.name")
    @Mapping(source = "createAllocationDTO.employeeEmail", target = "employee.email")
    @Mapping(target = "id", ignore = true)
    public abstract Allocation fromCreateAllocationDtoToEntity(CreateAllocationDTO createAllocationDTO, Room room);
}
