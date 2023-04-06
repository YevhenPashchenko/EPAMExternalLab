package com.epam.esm.dbfiller.repository.giftcertificates;

import com.epam.esm.dbfiller.entity.giftcertificates.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * API provides methods for basic CR operations with {@link Receipt} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
