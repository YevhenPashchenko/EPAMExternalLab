package com.epam.esm.giftcertificates.services;

import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.GiftCertificateSortingParameters;
import com.epam.esm.giftcertificates.entities.Tag;

import java.util.List;

/**
 * Interface that provides methods that must be implemented for operations with {@link GiftCertificate}
 * in service layer.
 *
 * @author Yevhen Pashchenko
 */
public interface GiftCertificateService {

    /**
     * Method that prepares data from {@link GiftCertificateDto} to convert them in {@link GiftCertificate} and then passes
     * to repository layer method which should create new {@link GiftCertificate} in database.
     * @param giftCertificateDTO {@link GiftCertificateDto}.
     */
    void create(GiftCertificateDto giftCertificateDTO);

    /**
     * Method that passes current page number to repository layer method which should return list of {@link GiftCertificate}
     * by this page number, then calls repository layer method which should return list of {@link Tag} for each
     * {@link GiftCertificate} corresponding to it and converts this data to list of {@link GiftCertificateDto}.
     * @param pageNumber current page number.
     * @return list of {@link GiftCertificateDto}.
     */
    List<GiftCertificateDto> get(int pageNumber);

    /**
     * Method that passes {@link GiftCertificate} id to repository layer method which should return {@link GiftCertificate}
     * by this id, then calls repository layer method which should return list of {@link Tag} corresponding to it and
     * converts this data to {@link GiftCertificateDto}.
     * @param id {@link GiftCertificate} id.
     * @return {@link GiftCertificateDto}.
     */
    GiftCertificateDto getById(long id);

    /**
     * Method that converts {@link GiftCertificateSortingParametersDto} to {@link GiftCertificateSortingParameters}
     * then calls repository layer method which should return list of {@link GiftCertificate} sorted by current page
     * number and parameters which passes to method as arguments. Then calls repository layer method which should return
     * list of {@link Tag} corresponding to each {@link GiftCertificate} in this list and converts this data to
     * {@link GiftCertificateDto}.
     * @param pageNumber current page number.
     * @param giftCertificateSortingParametersDto {@link GiftCertificateSortingParametersDto}.
     * @return list of {@link GiftCertificateDto}.
     */
    List<GiftCertificateDto> getByParameters(int pageNumber,
                                             GiftCertificateSortingParametersDto giftCertificateSortingParametersDto);

    /**
     * Method that checks which fields in {@link GiftCertificateDto} that passes as argument contains data and for each
     * of not empty field calls corresponding repository layer method with value of this field as argument of this method
     * and this method should update corresponding {@link GiftCertificate} column in database.
     * @param giftCertificateDTO {@link GiftCertificateDto}.
     */
    void update(GiftCertificateDto giftCertificateDTO);

    /**
     * Method that passes {@link GiftCertificate} id to repository layer method which should delete {@link GiftCertificate}
     * with this id from database.
     * @param id {@link GiftCertificate} id.
     */
    void delete(long id);
}
