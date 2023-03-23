package com.epam.esm.authorization.mapper;

import com.epam.esm.authorization.dto.UpdateClientDto;
import com.epam.esm.authorization.entity.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.util.StringUtils;

import java.util.Set;

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
    @Mapping(source = "scopes", target = "scopes", qualifiedByName = "setToString")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClient(@MappingTarget Client client, UpdateClientDto updateClient);

    @Named("setToString")
    static String setToString(Set<String> scopes) {
        return StringUtils.collectionToCommaDelimitedString(scopes);
    }
}
