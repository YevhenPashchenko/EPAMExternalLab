package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * API provides methods for basic CR operations with {@link Receipt} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Page<Receipt> findAllByEmail(String email, Pageable pageable);
}
