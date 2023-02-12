package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CR operations with {@link Recipe} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  Page<Recipe> getAllByPersonId(Long id, Pageable pageable);
}
