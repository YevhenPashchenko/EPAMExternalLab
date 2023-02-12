package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.aggregation.CountTagsId;
import com.epam.esm.giftcertificates.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * API provides methods for basic CRD operations with {@link Tag} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

  Optional<Tag> findTagByName(String name);

  Set<Tag> findTagsByGiftCertificatesId(Long id);

  Page<Tag> findTagByIdIn(List<Long> id, Pageable pageable);

  @Query(
      "SELECT count(t) AS tagCount, t.id AS tagId FROM tag t "
          + "LEFT JOIN t.giftCertificates gift_certificate "
          + "LEFT JOIN gift_certificate.recipes r "
          + "WHERE r.totalCost = (SELECT max(r.totalCost) FROM recipe r WHERE r.person.id = :id) "
          + "AND r.person.id = :id GROUP BY t.id")
  List<CountTagsId> findCountTagsInPersonMaxCostRecipe(Long id);
}
