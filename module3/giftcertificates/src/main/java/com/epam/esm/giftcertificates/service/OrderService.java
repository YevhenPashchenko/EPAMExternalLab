package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.OrderDto;
import com.epam.esm.giftcertificates.entity.Order;
import com.epam.esm.giftcertificates.entity.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link Order} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface OrderService {

  /**
   * Saves {@link Order} object.
   *
   * @param id {@link User} object {@code id}.
   * @param orderDto {@link OrderDto} object.
   * @return {@link Order} object.
   */
  EntityModel<OrderDto> createOrder(Long id, OrderDto orderDto);

  /**
   * Returns a list of {@link User} {@link OrderDto} objects from given {@code page} and {@code
   * size}.
   *
   * @param id {@link User} object {@code id}.
   * @param page {@code page}.
   * @param size {@code size}.
   * @return list of {@link OrderDto} objects.
   */
  PagedModel<OrderDto> getAllOrderDto(Long id, int page, int size);

  /**
   * Returns an {@link OrderDto} object by {@code id}.
   *
   * @param id object {@code id}.
   * @return {@link OrderDto} object.
   */
  EntityModel<OrderDto> getOrderDtoById(Long id);
}
