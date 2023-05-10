package com.epam.esm.giftcertificates.service;

import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.Receipt;
import com.epam.esm.giftcertificates.entity.Tag;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;

/**
 * API provides methods for services {@link Tag} objects data before CRD operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface TagService {

    /**
     * Saves {@link Tag} object.
     *
     * @param tag {@link TagDto} object.
     * @return {@link TagDto} object.
     */
    EntityModel<TagDto> createTag(TagDto tag);

    /**
     * Returns a list of {@link TagDto} objects from given {@code page} and {@code size}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link TagDto} objects.
     */
    PagedModel<TagDto> getAllTags(int page, int size);

    /**
     * Returns a {@link Tag} object by {@link Tag} object {@code name}.
     *
     * @param name {@code name}.
     * @return {@link Tag} object.
     */
    Tag getTagByName(String name);

    /**
     * Returns a list of {@link TagDto} objects from given {@code page}, {@code size} and {@link Receipt} object {@code
     * email}.
     *
     * @param page {@code page}.
     * @param size {@code size}.
     * @return list of {@link TagDto} objects.
     */
    PagedModel<TagDto> getMostWidelyUsedTagsFromPersonMaxCostReceipt(int page, int size);

    /**
     * Deletes {@link Tag} object by its {@code name}.
     *
     * @param name {@code name}.
     * @return {@link TagDto} object.
     */
    EntityModel<TagDto> deleteTag(String name);

    /**
     * Checks if {@link Tag} object by given {@code name} exist.
     *
     * @param name {@code name}.
     * @return true if {@link Tag} object with this {@code name} exist, otherwise false.
     */
    boolean isExist(String name);
}
