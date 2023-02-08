package com.epam.esm.giftcertificates.unit.service.impl;

import com.epam.esm.giftcertificates.assembler.OrderDtoAssembler;
import com.epam.esm.giftcertificates.repository.OrderRepository;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.OrderDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Order;
import com.epam.esm.giftcertificates.handler.exception.OrderNotFoundException;
import com.epam.esm.giftcertificates.handler.exception.OrderTotalCostCalculationException;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.OrderService;
import com.epam.esm.giftcertificates.service.UserService;
import com.epam.esm.giftcertificates.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;

class OrderServiceImplTest {

  private final GiftCertificateService giftCertificateService = mock(GiftCertificateService.class);
  private final UserService userService = mock(UserService.class);
  private final OrderDtoAssembler orderDtoAssembler = mock(OrderDtoAssembler.class);
  private final PagedResourcesAssembler<Order> pagedResourcesAssembler =
      mock(PagedResourcesAssembler.class);
  private final OrderRepository orderRepository = mock(OrderRepository.class);
  private final OrderService orderService =
      new OrderServiceImpl(
          giftCertificateService, userService, orderDtoAssembler, pagedResourcesAssembler, orderRepository);

  @Test
  void createOrder_shouldReturnOrderDtoEntityModel_whenExecutedNormally() {
    // GIVEN
    var orderDto = new OrderDto();
    orderDto.setGiftCertificates(List.of(new GiftCertificateDto()));

    var giftCertificate = new GiftCertificate();
    giftCertificate.setPrice(BigDecimal.ONE);

    given(giftCertificateService.getGiftCertificateById(null)).willReturn(giftCertificate);
    given(orderDtoAssembler.toModel(any(Order.class))).willReturn(new OrderDto());

    // WHEN
    var result = orderService.createOrder(0L, orderDto);

    // THEN
    assertEquals(EntityModel.of(new OrderDto()), result);
  }

  @Test
  void
      createOrder_shouldThrowOrderTotalCostCalculationException_whenGiftCertificatePriceIncorrect() {
    // THEN
    assertThrows(
        OrderTotalCostCalculationException.class,
        () -> orderService.createOrder(0L, new OrderDto()));
  }

  @Test
  void getAllOrderDto_shouldCallsPagedResourcesAssemblerToModel_whenExecutedNormally() {
    // WHEN
    orderService.getAllOrderDto(0L, 0, 2);

    // THEN
    then(pagedResourcesAssembler)
        .should(atLeastOnce())
        .toModel(orderRepository.getAllByUserId(0L, PageRequest.of(0, 2)), orderDtoAssembler);
  }

  @Test
  void getOrderDtoById_shouldReturnOrderDtoEntityModel_whenOrderWithThisIdExist() {
    // GIVEN
    given(orderRepository.findById(anyLong())).willReturn(Optional.of(new Order()));
    given(orderDtoAssembler.toModel(any(Order.class))).willReturn(new OrderDto());

    // WHEN
    var result = orderService.getOrderDtoById(0L);

    // THEN
    assertEquals(EntityModel.of(new OrderDto()), result);
  }

  @Test
  void getOrderDtoById_shouldThrowOrderNotFoundException_whenOrderWithThisIdNotExist() {
    // GIVEN
    given(orderRepository.findById(anyLong())).willReturn(Optional.empty());

    // THEN
    assertThrows(OrderNotFoundException.class, () -> orderService.getOrderDtoById(0L));
  }
}
