package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides methods that must be implemented for CRUD operations with {@link Tag}
 * in repository layer.
 *
 * @author Yevhen Pashchenko
 */
public interface TagDao {

    /**
     * Method that create new {@link Tag} in database.
     * @param tag {@link Tag} entity which data will be passed in database.
     * @return created {@link Tag} id.
     */
    long create(Tag tag);

    /**
     * Method that select {@link Tag}.
     * @return list of selected {@link Tag}.
     */
    List<Tag> get();

    /**
     * Method that select {@link Tag} by id.
     * @param id {@link Tag}.
     * @return {@link Optional<Tag>} if entity with this id exist in database or empty {@link Optional}.
     */
    Optional<Tag> getById(long id);

    /**
     * Method that select {@link Tag} by {@link GiftCertificate} id.
     * @param id {@link GiftCertificate}.
     * @return list of selected {@link Tag}.
     */
    List<Tag> getByGiftCertificateId(long id);

    /**
     * Method that delete {@link Tag} by id.
     * @param id {@link Tag}.
     */
    void delete(long id);
}
