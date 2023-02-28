package com.epam.esm.dbfiller.repository;

import com.epam.esm.dbfiller.entity.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * API provides methods for basic CRUD operations with {@link GiftCertificate} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

}
