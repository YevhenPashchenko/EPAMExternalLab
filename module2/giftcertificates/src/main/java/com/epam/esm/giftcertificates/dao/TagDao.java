package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;

import java.util.List;
import java.util.Optional;

/**
 * API provides methods for basic CRD operations with {@link TagDao} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface TagDao {

    /**
     * Saves {@link Tag} object.
     * @param tag given {@link Tag} object which data will be saved.
     * @return saved {@link Tag} object {@code id}.
     */
    long createTag(Tag tag);

    /**
     * Returns list of {@link Tag} objects.
     * @return list of {@link Tag} objects.
     */
    List<Tag> getListOfTag();

    /**
     * Returns {@link Optional} object with {@link Tag} object by its {@code id} if it with this id exists otherwise
     * returns empty {@link Optional} object.
     * @param id {@link Tag} object {@code id}.
     * @return {@link Optional} object.
     */
    Optional<Tag> getTagById(long id);

    /**
     * Returns list of {@link Tag} objects referenced with {@link GiftCertificate} object by its {@code id}.
     * @param id {@link GiftCertificate} object {@code id}.
     * @return list of {@link Tag} objects.
     */
    List<Tag> getListOfTagByGiftCertificateId(long id);

    /**
     * Deletes {@link Tag} object by its {@code id}.
     * @param id given {@code id}.
     */
    void deleteTag(long id);
}
