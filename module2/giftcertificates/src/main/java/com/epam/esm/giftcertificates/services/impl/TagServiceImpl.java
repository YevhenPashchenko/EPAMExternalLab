package com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.TagDao;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.handlers.exceptions.TagNotFoundException;
import com.epam.esm.giftcertificates.mappers.EntityDtoMapper;
import com.epam.esm.giftcertificates.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDAO;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public long create(TagDto tagDTO) {
        return tagDAO.create(entityDtoMapper.tagDtoToTag(tagDTO));
    }

    @Override
    public List<TagDto> get() {
        return tagDAO.get().stream()
                .map(entityDtoMapper::tagToTagDto)
                .toList();
    }

    @Override
    public TagDto getById(long id) {
        return entityDtoMapper.tagToTagDto(tagDAO.getById(id).orElseThrow(() -> new TagNotFoundException(id)));
    }

    @Override
    public List<TagDto> getByGiftCertificateId(long id) {
        return tagDAO.getByGiftCertificateId(id).stream()
                .map(entityDtoMapper::tagToTagDto)
                .toList();
    }

    @Override
    public void delete(long id) {
        tagDAO.delete(id);
    }
}
