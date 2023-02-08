package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}
