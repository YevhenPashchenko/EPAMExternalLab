package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.handler.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.service.TagService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    public void createGiftCertificate(GiftCertificateDto giftCertificateDto) {
        var giftCertificate = entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDto);
        giftCertificate.setId(giftCertificateDao.createGiftCertificate(giftCertificate));
        getEachTagIdOrCreateNew(giftCertificateDto.getTags());
        giftCertificateDto.getTags().forEach(tagDto -> createGiftCertificateToTagLinks(giftCertificate.getId(), tagDto.getId()));
    }

    private void getEachTagIdOrCreateNew(List<TagDto> listOfTagDto) {
        var tagsAsMap = tagService.getListOfTagDto()
                .stream()
                .collect(Collectors.toMap(TagDto::getName, TagDto::getId));
        listOfTagDto.forEach(tagDto -> tagDto.setId(tagsAsMap
                .computeIfAbsent(tagDto.getName(), tagName -> tagService.createTag(tagDto))));
    }

    private void createGiftCertificateToTagLinks(long giftCertificateId, long tagId) {
        var giftCertificateToTagDTO = new GiftCertificateToTagDto();
        giftCertificateToTagDTO.setGiftCertificateId(giftCertificateId);
        giftCertificateToTagDTO.setTagId(tagId);
        giftCertificateToTagService.createGiftCertificateToTagLink(giftCertificateToTagDTO);
    }

    @Override
    public List<GiftCertificateDto> getListOfGiftCertificateDtoForPage(int pageNumber) {
        var listOfGiftCertificateDto = giftCertificateDao.getListOfGiftCertificateForPage(pageNumber)
                .stream()
                .map(entityDtoMapper::giftCertificateToGiftCertificateDto)
                .toList();
        getTagsForGiftCertificate(listOfGiftCertificateDto);
        return listOfGiftCertificateDto;
    }

    private void getTagsForGiftCertificate(List<GiftCertificateDto> listOfGiftCertificateDto) {
        listOfGiftCertificateDto.forEach(giftCertificateDto ->
                giftCertificateDto.setTags(tagService.getListOfTagDtoByGiftCertificateId(giftCertificateDto.getId())));
    }

    @Override
    public GiftCertificateDto getGiftCertificateDtoById(long id) {
        var giftCertificateDto = entityDtoMapper
                .giftCertificateToGiftCertificateDto(giftCertificateDao.getGiftCertificateById(id)
                        .orElseThrow(() -> new GiftCertificateNotFoundException(id)));
        giftCertificateDto.setTags(tagService.getListOfTagDtoByGiftCertificateId(id));
        return giftCertificateDto;
    }

    @Override
    public List<GiftCertificateDto> getListOfGiftCertificateDtoByParametersForPage(int pageNumber,
                                                                                   GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
        var giftCertificateSortingParameters = entityDtoMapper
                .giftCertificateSortingParametersDtoToGiftCertificateSortingParameters(giftCertificateSortingParametersDto);
        var listOfGiftCertificateDto = giftCertificateDao
                .getListOfGiftCertificateByParametersForPage(pageNumber, giftCertificateSortingParameters)
                .stream()
                .map(entityDtoMapper::giftCertificateToGiftCertificateDto)
                .toList();
        getTagsForGiftCertificate(listOfGiftCertificateDto);
        return listOfGiftCertificateDto;
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateGiftCertificate(GiftCertificateDto giftCertificateDto) {
        var giftCertificate = entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDto);
        if (giftCertificate.getName() != null) {
            giftCertificateDao.updateGiftCertificateName(giftCertificate);
        }
        if (giftCertificate.getDescription() != null) {
            giftCertificateDao.updateGiftCertificateDescription(giftCertificate);
        }
        if (giftCertificate.getPrice() != null) {
            giftCertificateDao.updateGiftCertificatePrice(giftCertificate);
        }
        if (giftCertificate.getDuration() != 0) {
            giftCertificateDao.updateGiftCertificateDuration(giftCertificate);
        }
    }

    @Override
    public void deleteGiftCertificate(long id) {
        giftCertificateDao.deleteGiftCertificate(id);
    }
}
