package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CR operations with {@link Order} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  Page<Order> getAllByUserId(Long id, Pageable pageable);
}
