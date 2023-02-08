package com.epam.esm.giftcertificates.assembler;

import com.epam.esm.giftcertificates.controller.GiftCertificateController;
import com.epam.esm.giftcertificates.controller.OrderController;
import com.epam.esm.giftcertificates.controller.TagController;
import com.epam.esm.giftcertificates.controller.UserController;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.OrderDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.UserDto;
import com.epam.esm.giftcertificates.entity.Order;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import lombok.NonNull;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderDtoAssembler extends RepresentationModelAssemblerSupport<Order, OrderDto> {

  private final EntityDtoMapper entityDtoMapper;

  public OrderDtoAssembler(EntityDtoMapper entityDtoMapper) {
    super(UserController.class, OrderDto.class);
    this.entityDtoMapper = entityDtoMapper;
  }

  @Override
  public OrderDto toModel(@NonNull Order order) {
    var orderDto = entityDtoMapper.orderToOrderDto(order);
    orderDto.setUserDto(entityDtoMapper.userToUserDto(order.getUser()));
    orderDto
        .getGiftCertificates()
        .forEach(
            giftCertificateDto -> {
              giftCertificateDto.getTags().forEach(this::addSelfLinkToTagDto);
              addSelfLinkToGiftCertificateDto(giftCertificateDto);
            });
    addSelfLinkToUserDto(orderDto.getUserDto());
    addSelfLinkToOrderDto(orderDto);
    return orderDto;
  }

  private void addSelfLinkToTagDto(TagDto tagDto) {
    tagDto.add(linkTo(methodOn(TagController.class).getTagDtoById(tagDto.getId())).withSelfRel());
  }

  private void addSelfLinkToGiftCertificateDto(GiftCertificateDto giftCertificateDto) {
    giftCertificateDto.add(
        linkTo(
                methodOn(GiftCertificateController.class)
                    .getGiftCertificateById(giftCertificateDto.getId()))
            .withSelfRel());
  }

  private void addSelfLinkToUserDto(UserDto userDto) {
    userDto.add(
        linkTo(methodOn(UserController.class).getUserDtoById(userDto.getId())).withSelfRel());
  }

  private void addSelfLinkToOrderDto(OrderDto orderDto) {
    orderDto.add(
        linkTo(methodOn(OrderController.class).getOrderDtoById(orderDto.getId())).withSelfRel());
  }
}
