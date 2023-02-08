package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.OrderDtoAssembler;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.handler.exception.OrderTotalCostCalculationException;
import com.epam.esm.giftcertificates.repository.OrderRepository;
import com.epam.esm.giftcertificates.dto.OrderDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Order;
import com.epam.esm.giftcertificates.handler.exception.OrderNotFoundException;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.OrderService;
import com.epam.esm.giftcertificates.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final GiftCertificateService giftCertificateService;
  private final UserService userService;
  private final OrderDtoAssembler orderDtoAssembler;
  private final PagedResourcesAssembler<Order> pagedResourcesAssembler;
  private final OrderRepository orderRepository;

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public EntityModel<OrderDto> createOrder(Long id, OrderDto orderDto) {
    var order = new Order();
    var giftCertificates =
        orderDto.getGiftCertificates().stream()
            .map(GiftCertificateDto::getId)
            .map(giftCertificateService::getGiftCertificateById)
            .toList();
    order.setGiftCertificates(giftCertificates);
    order.setTotalCost(
        giftCertificates.stream()
            .map(GiftCertificate::getPrice)
            .reduce(BigDecimal::add)
            .orElseThrow(OrderTotalCostCalculationException::new));
    order.setUser(userService.getUserById(id));
    orderRepository.save(order);
    return EntityModel.of(orderDtoAssembler.toModel(order));
  }

  @Override
  public PagedModel<OrderDto> getAllOrderDto(Long id, int page, int size) {
    return pagedResourcesAssembler.toModel(
        orderRepository.getAllByUserId(id, PageRequest.of(page, size)), orderDtoAssembler);
  }

  @Override
  public EntityModel<OrderDto> getOrderDtoById(Long id) {
    return EntityModel.of(
        orderDtoAssembler.toModel(
            orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id))));
  }
}
