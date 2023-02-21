package com.epam.esm.dbfiller.repository;

import com.epam.esm.dbfiller.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CRD operations with {@link Tag} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

}
