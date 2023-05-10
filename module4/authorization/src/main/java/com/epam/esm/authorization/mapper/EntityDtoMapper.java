package com.epam.esm.authorization.mapper;

import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.entity.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EntityDtoMapper {

    /**
     * Update {@link Client} object fields value from not null {@link UpdateClientDto} object fields value.
     *
     * @param client       converted object.
     * @param updateClient object to convert.
     * @see Client
     * @see UpdateClientDto
     */
    @Mapping(target = "clientScopes", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClient(@MappingTarget Client client, UpdateClientDto updateClient);
}
