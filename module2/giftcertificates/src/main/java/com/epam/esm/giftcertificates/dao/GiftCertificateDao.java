package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.GiftCertificateSortingParameters;

import java.util.List;
import java.util.Optional;

/**
 * API provides methods for basic CRUD operations with {@link GiftCertificate} objects.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateDao {

    /**
     * Saves {@link GiftCertificate} object.
     * @param giftCertificate given {@link GiftCertificate} object which data will be saved.
     * @return saved {@link GiftCertificate} object {@code id}.
     */
    long createGiftCertificate(GiftCertificate giftCertificate);

    /**
     * Returns list of {@link GiftCertificate} objects from given {@code page}.
     * @param pageNumber {@code page}.
     * @return list of {@link GiftCertificate} objects.
     */
    List<GiftCertificate> getListOfGiftCertificateForPage(int pageNumber);

    /**
     * Returns {@link Optional} object with {@link GiftCertificate} object by its {@code id} if it with this id exists
     * otherwise returns empty {@link Optional} object.
     * @param id {@link GiftCertificate} object {@code id}.
     * @return {@link Optional} object.
     */
    Optional<GiftCertificate> getGiftCertificateById(long id);

    /**
     * Returns list of {@link GiftCertificate} objects by parameters given in {@link GiftCertificateSortingParameters}
     * object with given {@code page}.
     * @param pageNumber {@code page}.
     * @param giftCertificateSortingParameters {@link GiftCertificateSortingParameters} object.
     * @return list of {@link GiftCertificate} objects.
     */
    List<GiftCertificate> getListOfGiftCertificateByParametersForPage(int pageNumber,
                                                                      GiftCertificateSortingParameters giftCertificateSortingParameters);

    /**
     * Changes {@link GiftCertificate} object {@code name}.
     * @param giftCertificate {@link GiftCertificate} object with new {@code name}.
     */
    void updateGiftCertificateName(GiftCertificate giftCertificate);

    /**
     * Changes {@link GiftCertificate} object {@code description}.
     * @param giftCertificate {@link GiftCertificate} object with new {@code description}.
     */
    void updateGiftCertificateDescription(GiftCertificate giftCertificate);

    /**
     * Changes {@link GiftCertificate} object {@code price}.
     * @param giftCertificate {@link GiftCertificate} object with new {@code price}.
     */
    void updateGiftCertificatePrice(GiftCertificate giftCertificate);

    /**
     * Changes {@link GiftCertificate} object {@code duration}.
     * @param giftCertificate {@link GiftCertificate} object with new {@code duration}.
     */
    void updateGiftCertificateDuration(GiftCertificate giftCertificate);

    /**
     * Deletes {@link GiftCertificate} object by its {@code id}.
     * @param id {@code id}.
     */
    void deleteGiftCertificate(long id);
}
