package com.epam.esm.giftcertificates.services;

import com.epam.esm.giftcertificates.entities.dto.TagDTO;

import java.util.List;

public interface TagService {

    long create(TagDTO tagDTO);

    List<TagDTO> get();

    TagDTO getById(long id);

    void delete(long id);
}
