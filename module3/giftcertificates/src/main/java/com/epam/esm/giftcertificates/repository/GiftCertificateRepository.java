package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * API provides methods for basic CRUD operations with {@link GiftCertificate} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateRepository
    extends JpaRepository<GiftCertificate, Long>, GiftCertificateRepositoryCustom {

  @Query(
      "SELECT DISTINCT new com.epam.esm.giftcertificates.entity.GiftCertificate(g.id, g.name, g.description, g.price, "
          + "g.duration, g.createDate, g.lastUpdateDate) FROM gift_certificate g LEFT JOIN g.tags g_t "
          + "WHERE (:tagName IS NULL OR g_t.name = :tagName) AND (:partName IS NULL OR g.name LIKE :partName) "
          + "AND (:partDescription IS NULL OR g.description LIKE :partDescription)")
  Page<GiftCertificate> findByParameters(
      String tagName, String partName, String partDescription, Pageable pageable);
}
