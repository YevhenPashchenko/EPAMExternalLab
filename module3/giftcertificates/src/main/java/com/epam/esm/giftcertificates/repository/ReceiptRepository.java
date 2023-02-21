package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CR operations with {@link Receipt} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Page<Receipt> getAllByPersonId(Long id, Pageable pageable);
}
