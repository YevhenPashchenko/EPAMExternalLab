package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.GiftCertificateController;
import com.epam.esm.giftcertificates.controller.ReceiptController;
import com.epam.esm.giftcertificates.controller.TagController;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.ReceiptDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ReceiptDtoAssembler extends RepresentationModelAssemblerSupport<Receipt, ReceiptDto> {

    private final EntityDtoMapper entityDtoMapper;

    public ReceiptDtoAssembler(EntityDtoMapper entityDtoMapper) {
        super(ReceiptController.class, ReceiptDto.class);
        this.entityDtoMapper = entityDtoMapper;
    }

    @NonNull
    @Override
    public ReceiptDto toModel(@NonNull Receipt receipt) {
        var receiptDto = entityDtoMapper.receiptToReceiptDto(receipt);
        receiptDto.getGiftCertificates().forEach(giftCertificateDto -> {
                giftCertificateDto.getTags().forEach(this::addSelfLinkToTag);
                addSelfLinkToGiftCertificate(giftCertificateDto);
            }
        );
        receiptDto.add(linkTo(methodOn(ReceiptController.class).getById(receipt.getId())).withSelfRel());
        return receiptDto;
    }

    private void addSelfLinkToTag(TagDto tagDto) {
        tagDto.add(linkTo(methodOn(TagController.class).getByName(tagDto.getName())).withSelfRel());
    }

    private void addSelfLinkToGiftCertificate(GiftCertificateDto giftCertificateDto) {
        giftCertificateDto.add(
            linkTo(methodOn(GiftCertificateController.class).getByName(giftCertificateDto.getName())).withSelfRel());
    }
}
