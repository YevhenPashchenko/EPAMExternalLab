package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.TagNamesDto;
import com.epam.esm.giftcertificates.dto.UpdateGiftCertificateDto;
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
     * @param giftCertificate {@link GiftCertificateDto} object.
     * @return {@link GiftCertificateDto} object.
     */
    EntityModel<GiftCertificateDto> createGiftCertificate(GiftCertificateDto giftCertificate);

    /**
     * Returns a list of {@link GiftCertificateDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link GiftCertificateDto} objects.
     */
    PagedModel<GiftCertificateDto> getAllGiftCertificates(int page, int size);

    /**
     * Returns a {@link GiftCertificate} object by given {@link GiftCertificate} {@code name}.
     *
     * @param name {@code name}.
     * @return {@link GiftCertificate} object.
     */
    GiftCertificate getGiftCertificateByName(String name);

    /**
     * Returns a list of {@link GiftCertificateDto} objects selected and sorted by parameters, given in {@link
     * GiftCertificateSortingParametersDto} object and given from {@code page} and {@code size}.
     *
     * @param page                             {@code page}.
     * @param size                             {@code size}.
     * @param giftCertificateSortingParameters {@link GiftCertificateSortingParametersDto} object.
     * @return list of {@link GiftCertificateDto} objects.
     */
    PagedModel<GiftCertificateDto> getAllGiftCertificatesByParameters(int page, int size,
        GiftCertificateSortingParametersDto giftCertificateSortingParameters);

    /**
     * Returns a list of {@link GiftCertificateDto} objects selected by {@link Tag} objects, given in {@link
     * TagNamesDto} object from given {@code page} and {@code size}.
     *
     * @param page     {@code page}.
     * @param size     {@code size}.
     * @param tagNames {@link TagNamesDto} object.
     * @return list of {@link GiftCertificateDto} objects.
     */
    PagedModel<GiftCertificateDto> getAllGiftCertificatesByTags(int page, int size, TagNamesDto tagNames);

    /**
     * Changes values of fields {@link GiftCertificate} object.
     *
     * @param name                  {@link GiftCertificate} object {@code name}.
     * @param updateGiftCertificate {@link UpdateGiftCertificateDto} object.
     * @return {@link GiftCertificateDto} object.
     */
    EntityModel<GiftCertificateDto> updateGiftCertificate(String name, UpdateGiftCertificateDto updateGiftCertificate);

    /**
     * Deletes {@link GiftCertificate} object by its {@code name}.
     *
     * @param name {@code name}.
     * @return {@link GiftCertificateDto} object.
     */
    EntityModel<GiftCertificateDto> deleteGiftCertificate(String name);
}
