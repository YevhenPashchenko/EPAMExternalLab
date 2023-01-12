package com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.handlers.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.mappers.EntityDtoMapper;
import com.epam.esm.giftcertificates.services.GiftCertificateService;
import com.epam.esm.giftcertificates.services.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.services.TagService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDao giftCertificateDao;
    private final TagService tagService;
    private final GiftCertificateToTagService giftCertificateToTagService;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void create(GiftCertificateDto giftCertificateDto) {
        var giftCertificate = entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDto);
        giftCertificate.setId(giftCertificateDao.create(giftCertificate));
        getEachTagIdOrCreateNew(giftCertificateDto.getTags());
        createGiftCertificateToTagLinks(giftCertificateDto.getTags(), giftCertificate);
    }

    private void getEachTagIdOrCreateNew(List<TagDto> tagDtos) {
        var tagsMap = !tagDtos.isEmpty() ? tagService.get().stream()
                .collect(Collectors.toMap(TagDto::getName, TagDto::getId)) : new HashMap<String, Long>();
        tagDtos.forEach(tagDto -> tagDto.setId(tagsMap
                .computeIfAbsent(tagDto.getName(), tagName -> tagService.create(tagDto))));
    }

    private void createGiftCertificateToTagLinks(List<TagDto> tagDtos, GiftCertificate giftCertificate) {
        tagDtos.forEach(tagDTO -> {
                    var giftCertificateToTagDTO = new GiftCertificateToTagDto();
                    giftCertificateToTagDTO.setGiftCertificateId(giftCertificate.getId());
                    giftCertificateToTagDTO.setTagId(tagDTO.getId());
                    giftCertificateToTagService.create(giftCertificateToTagDTO);
                });
    }

    @Override
    public List<GiftCertificateDto> get(int pageNumber) {
        var giftCertificateDtos = giftCertificateDao.get(pageNumber).stream()
                .map(entityDtoMapper::giftCertificateToGiftCertificateDto)
                .toList();
        getTagsForGiftCertificate(giftCertificateDtos);
        return giftCertificateDtos;
    }

    private void getTagsForGiftCertificate(List<GiftCertificateDto> giftCertificateDtos) {
        giftCertificateDtos.forEach(giftCertificateDto ->
                giftCertificateDto.setTags(tagService.getByGiftCertificateId(giftCertificateDto.getId())));
    }

    @Override
    public GiftCertificateDto getById(long id) {
        var giftCertificateDto = entityDtoMapper
                .giftCertificateToGiftCertificateDto(giftCertificateDao.getById(id)
                        .orElseThrow(() -> new GiftCertificateNotFoundException(id)));
        giftCertificateDto.setTags(tagService.getByGiftCertificateId(id));
        return giftCertificateDto;
    }

    @Override
    public List<GiftCertificateDto> getByParameters(int pageNumber,
                                                    GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
        var giftCertificateSortingParameters = entityDtoMapper
                .giftCertificateSortingParametersDtoToGiftCertificateSortingParameters(giftCertificateSortingParametersDto);
        var giftCertificateDtos = giftCertificateDao
                .getByParameters(pageNumber, giftCertificateSortingParameters).stream()
                .map(entityDtoMapper::giftCertificateToGiftCertificateDto)
                .toList();
        getTagsForGiftCertificate(giftCertificateDtos);
        return giftCertificateDtos;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(GiftCertificateDto giftCertificateDTO) {
        var giftCertificate = entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDTO);
        if (giftCertificate.getName() != null) {
            giftCertificateDao.updateName(giftCertificate);
        }
        if (giftCertificate.getDescription() != null) {
            giftCertificateDao.updateDescription(giftCertificate);
        }
        if (giftCertificate.getPrice() != null) {
            giftCertificateDao.updatePrice(giftCertificate);
        }
        if (giftCertificate.getDuration() != 0) {
            giftCertificateDao.updateDuration(giftCertificate);
        }
    }

    @Override
    public void delete(long id) {
        giftCertificateDao.delete(id);
    }
}
