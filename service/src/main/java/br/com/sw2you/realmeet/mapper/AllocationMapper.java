package br.com.sw2you.realmeet.mapper;

import br.com.sw2you.realmeet.api.model.AllocationDTO;
import br.com.sw2you.realmeet.api.model.CreateAllocationDTO;
import br.com.sw2you.realmeet.domain.entity.Allocation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AllocationMapper {

    public abstract AllocationDTO fromEntityToDto(Allocation allocation);

    public abstract Allocation fromCreateAllocationDtoToEntity(CreateAllocationDTO createAllocationDTO);
}
