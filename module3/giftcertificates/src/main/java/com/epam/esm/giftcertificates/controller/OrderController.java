package com.epam.esm.giftcertificates.controller;

import com.epam.esm.giftcertificates.dto.OrderDto;
import com.epam.esm.giftcertificates.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Valid
public class OrderController {

  private final OrderService orderService;

  @PostMapping(
      value = "/{id}/orders",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<OrderDto> createOrder(
      @PathVariable("id") Long id, @RequestBody OrderDto orderDto) {
    return orderService.createOrder(id, orderDto);
  }

  @GetMapping(value = "/{id}/orders", produces = MediaType.APPLICATION_JSON_VALUE)
  public PagedModel<OrderDto> getAllOrderDto(
      @PathVariable("id") Long id,
      @RequestParam(defaultValue = "0") @Min(value = 0, message = "page must be not less 0")
          int page,
      @RequestParam(defaultValue = "20")
          @Range(min = 1, max = 100, message = "size must be between 1 and 100")
          int size) {
    return orderService.getAllOrderDto(id, page, size);
  }

  @GetMapping(value = "/orders/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public EntityModel<OrderDto> getOrderDtoById(@PathVariable("id") Long id) {
    return orderService.getOrderDtoById(id);
  }
}
