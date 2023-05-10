package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * API provides methods for basic CRUD operations with {@link GiftCertificate} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    Optional<GiftCertificate> findByName(String name);

    @Query("SELECT DISTINCT g FROM com.epam.esm.giftcertificates.entity.GiftCertificate g LEFT JOIN g.tags g_t "
        + "WHERE (:tagName IS NULL OR g_t.name = :tagName) AND (:partName IS NULL OR g.name LIKE :partName) "
        + "AND (:partDescription IS NULL OR g.description LIKE :partDescription)")
    Page<GiftCertificate> findByParameters(String tagName, String partName, String partDescription, Pageable pageable);

    @Query("SELECT g FROM com.epam.esm.giftcertificates.entity.GiftCertificate g LEFT JOIN g.tags g_t "
        + "WHERE g_t.name in :tagNames group by g.id having count(g.id) = :count")
    Page<GiftCertificate> findGiftCertificatesByTagNames(Set<String> tagNames, int count, Pageable pageable);

    List<GiftCertificate> deleteByName(String name);
}
