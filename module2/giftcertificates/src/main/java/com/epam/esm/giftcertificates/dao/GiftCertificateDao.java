package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.GiftCertificateSortingParameters;

import java.util.List;
import java.util.Optional;

/**
 * Interface that provides methods that must be implemented for CRUD operations with {@link GiftCertificate}
 * in repository layer.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateDao {

    /**
     * Method that creates new {@link GiftCertificate} in database.
     * @param giftCertificate {@link GiftCertificate} entity which data will be passed in database.
     * @return created {@link GiftCertificate} id.
     */
    long create(GiftCertificate giftCertificate);

    /**
     * Method that selects {@link GiftCertificate} for current page.
     * @param pageNumber current page number.
     * @return list of selected {@link GiftCertificate}.
     */
    List<GiftCertificate> get(int pageNumber);

    /**
     * Method that selects {@link GiftCertificate} by id.
     * @param id {@link GiftCertificate}.
     * @return {@link Optional<GiftCertificate>} if entity with this id exists in database or empty {@link Optional}.
     */
    Optional<GiftCertificate> getById(long id);

    /**
     * Method that selects {@link GiftCertificate} for current page by parameters that passed in
     * {@link GiftCertificateSortingParameters}.
     * @param pageNumber current page number.
     * @param giftCertificateSortingParameters {@link GiftCertificateSortingParameters}.
     * @return list of selected {@link GiftCertificate}.
     */
    List<GiftCertificate> getByParameters(int pageNumber, GiftCertificateSortingParameters giftCertificateSortingParameters);

    /**
     * Method that updates {@link GiftCertificate} name.
     * @param giftCertificate {@link GiftCertificate} with new name.
     */
    void updateName(GiftCertificate giftCertificate);

    /**
     * Method that updates {@link GiftCertificate} description.
     * @param giftCertificate {@link GiftCertificate} with new description.
     */
    void updateDescription(GiftCertificate giftCertificate);

    /**
     * Method that updates {@link GiftCertificate} price.
     * @param giftCertificate {@link GiftCertificate} with new price.
     */
    void updatePrice(GiftCertificate giftCertificate);

    /**
     * Method that updates {@link GiftCertificate} duration.
     * @param giftCertificate {@link GiftCertificate} with new duration.
     */
    void updateDuration(GiftCertificate giftCertificate);

    /**
     * Method that deletes {@link GiftCertificate} by id.
     * @param id {@link GiftCertificate}.
     */
    void delete(long id);
}
