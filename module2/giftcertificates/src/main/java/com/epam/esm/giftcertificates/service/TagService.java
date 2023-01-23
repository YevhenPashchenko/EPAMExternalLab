package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;

import java.util.List;

/**
 * API provides methods for services {@link Tag} objects data before CRD operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface TagService {

    /**
     * Saves {@link TagDto} object.
     * @param tagDTO {@link TagDto} object.
     * @return {@link Tag} {@code id}.
     */
    long createTag(TagDto tagDTO);

    /**
     * Returns a list of {@link TagDto} objects.
     * @return list of {@link TagDto} objects.
     */
    List<TagDto> getListOfTagDto();

    /**
     * Returns a {@link TagDto} object by {@link Tag} object {@code id}.
     * @param id {@code id}.
     * @return {@link TagDto} object.
     */
    TagDto getTagDtoById(long id);

    /**
     * Returns a list of {@link TagDto} objects that reference {@link GiftCertificate} object by its {@code id}.
     * @param id {@code id}.
     * @return list of {@link TagDto} objects.
     */
    List<TagDto> getListOfTagDtoByGiftCertificateId(long id);

    /**
     * Deletes {@link Tag} object by its {@code id}.
     * @param id {@code id}.
     */
    void deleteTag(long id);
}
