package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.dao.TagDao;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.handler.exceptions.TagNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDAO;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public long createTag(TagDto tagDTO) {
        return tagDAO.createTag(entityDtoMapper.tagDtoToTag(tagDTO));
    }

    @Override
    public List<TagDto> getListOfTagDto() {
        return tagDAO.getListOfTag()
                .stream()
                .map(entityDtoMapper::tagToTagDto)
                .toList();
    }

    @Override
    public TagDto getTagDtoById(long id) {
        return entityDtoMapper.tagToTagDto(tagDAO.getTagById(id)
                .orElseThrow(() -> new TagNotFoundException(id)));
    }

    @Override
    public List<TagDto> getListOfTagDtoByGiftCertificateId(long id) {
        return tagDAO.getListOfTagByGiftCertificateId(id)
                .stream()
                .map(entityDtoMapper::tagToTagDto)
                .toList();
    }

    @Override
    public void deleteTag(long id) {
        tagDAO.deleteTag(id);
    }
}
