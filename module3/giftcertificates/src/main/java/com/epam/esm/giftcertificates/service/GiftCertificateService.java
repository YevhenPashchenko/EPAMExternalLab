package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link GiftCertificate} objects data before CRUD operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateService {

    /**
     * Saves {@link GiftCertificate} object.
     *
     * @param giftCertificateDto {@link GiftCertificateDto} object.
     * @return {@link GiftCertificateDto} object.
     */
    EntityModel<GiftCertificateDto> createGiftCertificate(GiftCertificateDto giftCertificateDto);

    /**
     * Returns a list of {@link GiftCertificateDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link GiftCertificateDto} objects.
     */
    PagedModel<GiftCertificateDto> getAllGiftCertificates(int page, int size);

    /**
     * Returns a {@link GiftCertificateDto} object by given {@link GiftCertificate} {@code id}.
     *
     * @param id {@code id}.
     * @return {@link GiftCertificateDto} object.
     */
    EntityModel<GiftCertificateDto> getGiftCertificateDtoById(Long id);

    /**
     * Returns a {@link GiftCertificate} object by given {@link GiftCertificate} {@code id}.
     *
     * @param id {@code id}.
     * @return {@link GiftCertificate} object.
     */
    GiftCertificate getGiftCertificateById(Long id);

    /**
     * Returns a list of {@link GiftCertificateDto} objects selected and sorted by parameters, given in {@link
     * GiftCertificateSortingParametersDto} object and given from {@code page} and {@code size}.
     *
     * @param page                                {@code page}.
     * @param size                                {@code size}.
     * @param giftCertificateSortingParametersDto {@link GiftCertificateSortingParametersDto} object.
     * @return list of {@link GiftCertificateDto} objects.
     */
    PagedModel<GiftCertificateDto> getAllGiftCertificatesByParameters(int page, int size,
        GiftCertificateSortingParametersDto giftCertificateSortingParametersDto);

    /**
     * Returns a list of {@link GiftCertificateDto} objects selected by {@link Tag} objects, given in {@link
     * GiftCertificateDto} object from given {@code page} and {@code size}.
     *
     * @param page               {@code page}.
     * @param size               {@code size}.
     * @param giftCertificateDto {@link GiftCertificateDto} object.
     * @return list of {@link GiftCertificateDto} objects.
     */
    PagedModel<GiftCertificateDto> getAllGiftCertificatesByTags(int page, int size,
        GiftCertificateDto giftCertificateDto);

    /**
     * Changes values of fields {@link GiftCertificate} object.
     *
     * @param id                          {@link GiftCertificate} object {@code id}.
     * @param giftCertificateDtoForUpdate {@link GiftCertificateDtoForUpdate} object.
     * @return {@link GiftCertificateDto} object.
     */
    EntityModel<GiftCertificateDto> updateGiftCertificate(Long id,
        GiftCertificateDtoForUpdate giftCertificateDtoForUpdate);

    /**
     * Deletes {@link GiftCertificate} object by its {@code id}.
     *
     * @param id {@code id}.
     */
    void deleteGiftCertificate(Long id);
}
