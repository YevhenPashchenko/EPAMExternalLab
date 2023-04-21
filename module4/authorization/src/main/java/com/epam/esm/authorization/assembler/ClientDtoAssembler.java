package com.epam.esm.authorization.assembler;

import com.epam.esm.authorization.controller.ClientController;
import com.epam.esm.authorization.dto.ClientDto;
import com.epam.esm.authorization.entity.Client;
import com.epam.esm.authorization.entity.ClientScope;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

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
        clientDto.setScopes(client.getClientScopes().stream().map(ClientScope::getScope).collect(Collectors.toSet()));

        return clientDto.add(
            linkTo(methodOn(ClientController.class).getByClientId(clientDto.getClientId())).withSelfRel());
    }
}
