package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.GiftCertificateController;
import com.epam.esm.giftcertificates.controller.TagController;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GiftCertificateDtoAssembler extends
    RepresentationModelAssemblerSupport<GiftCertificate, GiftCertificateDto> {

    private final EntityDtoMapper entityDtoMapper;

    public GiftCertificateDtoAssembler(EntityDtoMapper entityDtoMapper) {
        super(GiftCertificateController.class, GiftCertificateDto.class);
        this.entityDtoMapper = entityDtoMapper;
    }

    @NonNull
    @Override
    public GiftCertificateDto toModel(@NonNull GiftCertificate giftCertificate) {
        var giftCertificateDto = entityDtoMapper.giftCertificateToGiftCertificateDto(giftCertificate);
        giftCertificateDto.getTags().forEach(this::addSelfLinkToTag);
        addSelfLinkToGiftCertificate(giftCertificateDto);
        return giftCertificateDto;
    }

    private void addSelfLinkToTag(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class).getByName(tagDto.getName())).withSelfRel());
    }

    private void addSelfLinkToGiftCertificate(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(
            linkTo(methodOn(GiftCertificateController.class).getByName(giftCertificateDto.getName())).withSelfRel());
    }
}
