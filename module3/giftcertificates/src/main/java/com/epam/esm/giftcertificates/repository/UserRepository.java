package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * API provides methods for basic CRD operations with {@link User} objects.
 *
 * @author Yevhen Pashchenko
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
