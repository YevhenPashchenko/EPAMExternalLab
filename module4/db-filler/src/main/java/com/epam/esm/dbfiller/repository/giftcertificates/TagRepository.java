package com.epam.esm.dbfiller.repository.giftcertificates;

import com.epam.esm.dbfiller.entity.giftcertificates.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * API provides methods for basic CRD operations with {@link Tag} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface TagRepository extends JpaRepository<Tag, Long> {

}
