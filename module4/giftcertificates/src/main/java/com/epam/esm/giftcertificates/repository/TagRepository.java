package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.entity.projection.TagsCountToTagsIdProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * API provides methods for basic CRD operations with {@link Tag} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findTagByName(String name);

    Set<Tag> findTagsByGiftCertificatesId(Long id);

    Page<Tag> findTagByIdIn(List<Long> id, Pageable pageable);

    @Query(
        "SELECT count(t) AS tagCount, t.id AS tagId FROM tag t "
            + "JOIN t.giftCertificates gift_certificate "
            + "JOIN gift_certificate.receipts r "
            + "WHERE r.totalCost = (SELECT max(r.totalCost) FROM receipt r WHERE r.email = :email) "
            + "AND r.email = :email GROUP BY t.id")
    List<TagsCountToTagsIdProjection> findCountTagsInPersonMaxCostReceipt(String email);

    List<Tag> deleteByName(String name);

    boolean existsByName(String name);
}
