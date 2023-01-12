package com.epam.esm.giftcertificates.services;

import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.Tag;

import java.util.List;

/**
 * Interface that provides methods that must be implemented for operations with {@link Tag} in service layer.
 *
 * @author Yevhen Pashchenko
 */
public interface TagService {

    /**
     * Method that prepares data from {@link TagDto} to convert them in {@link Tag} and then passes
     * to repository layer method which should create new {@link Tag} in database.
     * @param tagDTO {@link TagDto}.
     * @return id of created {@link Tag}.
     */
    long create(TagDto tagDTO);

    /**
     * Method that calls repository layer method which should return list of {@link Tag} and converts this data to
     * list of {@link TagDto}.
     * @return list of {@link TagDto}.
     */
    List<TagDto> get();

    /**
     * Method that passes {@link Tag} id to repository layer method which should return {@link Tag} by this id and
     * converts this data to {@link TagDto}.
     * @param id {@link Tag} id.
     * @return {@link TagDto}.
     */
    TagDto getById(long id);

    /**
     * Method that passes {@link GiftCertificate} id to repository layer method which should return list of {@link Tag}
     * that corresponds this {@link GiftCertificate} and converts this data to list of {@link TagDto}.
     * @param id {@link GiftCertificate} id.
     * @return list of {@link TagDto}.
     */
    List<TagDto> getByGiftCertificateId(long id);

    /**
     * Method that passes {@link Tag} id to repository layer method which should delete {@link Tag} with this id from database.
     * @param id {@link Tag} id.
     */
    void delete(long id);
}
