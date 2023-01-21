package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;

import java.util.List;

/**
 * API provides methods for services {@link GiftCertificate} objects data before CRUD operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateService {

    /**
     * Saves {@link GiftCertificate} object.
     * @param giftCertificateDto {@link GiftCertificateDto} object.
     */
    void createGiftCertificate(GiftCertificateDto giftCertificateDto);

    /**
     * Returns a list of {@link GiftCertificateDto} objects from given {@code page}.
     * @param pageNumber {@code page}.
     * @return list of {@link GiftCertificateDto} objects.
     */
    List<GiftCertificateDto> getListOfGiftCertificateDtoForPage(int pageNumber);

    /**
     * Returns a {@link GiftCertificateDto} object by given {@link GiftCertificate} {@code id}.
     * @param id {@code id}.
     * @return {@link GiftCertificateDto} object.
     */
    GiftCertificateDto getGiftCertificateDtoById(long id);

    /**
     * Returns a list of {@link GiftCertificateDto} objects selected and sorted by parameters, given in
     * {@link GiftCertificateSortingParametersDto} object and given from {@code page}.
     * @param pageNumber {@code page}.
     * @param giftCertificateSortingParametersDto {@link GiftCertificateSortingParametersDto} object.
     * @return list of {@link GiftCertificateDto} objects.
     */
    List<GiftCertificateDto> getListOfGiftCertificateDtoByParametersForPage(int pageNumber,
                                                                            GiftCertificateSortingParametersDto giftCertificateSortingParametersDto);

    /**
     * Changes values of fields {@link GiftCertificate} object.
     * @param giftCertificateDto {@link GiftCertificateDto} object.
     */
    void updateGiftCertificate(GiftCertificateDto giftCertificateDto);

    /**
     * Deletes {@link GiftCertificate} object by its {@code id}.
     * @param id {@code id}.
     */
    void deleteGiftCertificate(long id);
}
