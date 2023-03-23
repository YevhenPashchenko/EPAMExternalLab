package com.epam.esm.authorization.assembler;

import com.epam.esm.authorization.controller.ClientController;
import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.entity.Client;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ClientDtoAssembler extends RepresentationModelAssemblerSupport<Client, ClientDto> {

    public ClientDtoAssembler() {
        super(ClientController.class, ClientDto.class);
    }

    @NonNull
    @Override
    public ClientDto toModel(@NonNull Client client) {
        var clientDto = new ClientDto();
        clientDto.setClientId(client.getClientId());
        clientDto.setClientSecret(client.getClientSecret());
        clientDto.setScopes(StringUtils.commaDelimitedListToSet(client.getScopes()));

        return clientDto.add(
            linkTo(methodOn(ClientController.class).getByClientId(clientDto.getClientId())).withSelfRel());
    }
}
