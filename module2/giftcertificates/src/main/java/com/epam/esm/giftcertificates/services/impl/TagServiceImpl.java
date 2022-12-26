package com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.TagDAO;
import com.epam.esm.giftcertificates.entities.Tag;
import com.epam.esm.giftcertificates.entities.dto.TagDTO;
import com.epam.esm.giftcertificates.handlers.exceptions.TagNotFoundException;
import com.epam.esm.giftcertificates.services.TagService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDAO tagDAO;
    private final ModelMapper modelMapper;

    @Override
    public long create(TagDTO tagDTO) {
        return tagDAO.create(convertToEntity(tagDTO));
    }

    @Override
    public List<TagDTO> get() {
        List<TagDTO> tagDTOs = new ArrayList<>();
        List<Tag> tags = tagDAO.get();
        tags.forEach(tag -> tagDTOs.add(convertToDTO(tag)));
        return tagDTOs;
    }

    @Override
    public TagDTO getById(long id) {
        return convertToDTO(tagDAO.getById(id).orElseThrow(() -> new TagNotFoundException(id)));
    }

    @Override
    public void delete(long id) {
        tagDAO.delete(id);
    }

    private Tag convertToEntity(TagDTO tagDTO) {
        return modelMapper.map(tagDTO, Tag.class);
    }

    private TagDTO convertToDTO(Tag tag) {
        return modelMapper.map(tag, TagDTO.class);
    }
}
