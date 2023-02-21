package com.epam.esm.dbfiller.repository;

import com.epam.esm.dbfiller.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CR operations with {@link Receipt} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

}
