package com.epam.esm.dbfiller.repository.giftcertificates;

import com.epam.esm.dbfiller.entity.giftcertificates.GiftCertificate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * API provides methods for basic CRUD operations with {@link GiftCertificate} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

}
