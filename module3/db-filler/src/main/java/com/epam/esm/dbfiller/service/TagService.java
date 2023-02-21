package com.epam.esm.dbfiller.service;

import com.epam.esm.dbfiller.entity.Tag;

import java.util.List;

/**
 * API provides methods for services {@link Tag} objects data before CR operations with them.
 *
 * @author Yevhen Pashchenko
 */
public interface TagService {

    /**
     * Saves list of {@link Tag} objects.
     *
     * @param tags list of {@link Tag} objects.
     */
    void createTags(List<Tag> tags);

    /**
     * Returns a {@link Tag} object by {@link Tag} object {@code id}.
     *
     * @param id {@code id}.
     * @return {@link Tag}.
     */
    Tag getTagById(Long id);
}
