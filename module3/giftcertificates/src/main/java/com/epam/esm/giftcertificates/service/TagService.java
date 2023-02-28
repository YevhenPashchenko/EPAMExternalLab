package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.entity.Person;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

import java.util.Set;

/**
 * API provides methods for services {@link Tag} objects data before CRD operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface TagService {

    /**
     * Saves {@link Tag} object.
     *
     * @param tagDTO {@link TagDto} object.
     * @return {@link Tag} {@code id}.
     */
    EntityModel<TagDto> createTag(TagDto tagDTO);

    /**
     * Returns a list of {@link TagDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link TagDto} objects.
     */
    PagedModel<TagDto> getAllTags(int page, int size);

    /**
     * Returns a {@link TagDto} object by {@link Tag} object {@code id}.
     *
     * @param id {@code id}.
     * @return {@link TagDto} object.
     */
    EntityModel<TagDto> getTagById(long id);

    /**
     * Returns a list of {@link TagDto} objects from given {@code page}, {@code size} and {@link Person} object {@code
     * id}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @param id   {@link Person} object {@code id}.
     * @return list of {@link TagDto} objects.
     */
    PagedModel<TagDto> getMostWidelyUsedTagsFromPersonMaxCostReceipt(int page, int size, Long id);

    /**
     * Returns a {@link TagDto} object by {@link Tag} object {@code name}.
     *
     * @param name {@code name}.
     * @return {@link TagDto}.
     */
    TagDto getTagByName(String name);

    /**
     * Returns a list of {@link Tag} objects that reference {@link GiftCertificate} object by its {@code id}.
     *
     * @param id {@code id}.
     * @return list of {@link Tag} objects.
     */
    Set<Tag> getTagsByGiftCertificateId(Long id);

    /**
     * Deletes {@link Tag} object by its {@code id}.
     *
     * @param id {@code id}.
     */
    void deleteTag(long id);
}
