package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDAO {

    long create(Tag tag);

    List<Tag> get();

    Optional<Tag> getById(long id);

    void delete(long id);
}
