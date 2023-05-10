package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.giftcertificates.constant.HttpErrorCodes;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.dto.TagNamesDto;
import com.epam.esm.giftcertificates.dto.UpdateGiftCertificateDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.entity.Tag;
import com.epam.esm.giftcertificates.handler.exception.EntityNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.service.GiftCertificateService;
import com.epam.esm.giftcertificates.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateRepository giftCertificateRepository;
    private final TagService tagService;
    private final EntityDtoMapper entityDtoMapper;
    private final GiftCertificateDtoAssembler giftCertificateDtoAssembler;
    private final PagedResourcesAssembler<GiftCertificate> pagedResourcesAssembler;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EntityModel<GiftCertificateDto> createGiftCertificate(GiftCertificateDto giftCertificateDto) {
        var existingTags = getExistingTags(giftCertificateDto.getTags());
        existingTags.forEach(tag -> giftCertificateDto.getTags().remove(entityDtoMapper.tagToTagDto(tag)));
        var giftCertificate = entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDto);
        giftCertificateDto.getTags().forEach(tag -> giftCertificate.addTag(entityDtoMapper.tagDtoToTag(tag)));
        giftCertificateRepository.save(giftCertificate);
        giftCertificate.getTags().addAll(existingTags);
        return EntityModel.of(giftCertificateDtoAssembler.toModel(giftCertificate));
    }

    private Set<Tag> getExistingTags(Set<TagDto> tags) {
        return tags.stream()
            .filter(tag -> tagService.isExist(tag.getName()))
            .map(tag -> tagService.getTagByName(tag.getName()))
            .collect(
                Collectors.toSet());
    }

    @Override
    public PagedModel<GiftCertificateDto> getAllGiftCertificates(int page, int size) {
        return pagedResourcesAssembler.toModel(giftCertificateRepository.findAll(PageRequest.of(page, size)),
            giftCertificateDtoAssembler);
    }

    @Override
    public GiftCertificate getGiftCertificateByName(String name) {
        return getGiftCertificate(name);
    }

    private GiftCertificate getGiftCertificate(String name) {
        return giftCertificateRepository.findByName(name)
            .orElseThrow(
                () -> new EntityNotFoundException(getErrorMessage(name), HttpErrorCodes.GIFT_CERTIFICATE_NOT_FOUND));
    }

    private String getErrorMessage(String name) {
        return "Requested gift certificate not found (name = " + name + ")";
    }

    @Override
    public PagedModel<GiftCertificateDto> getAllGiftCertificatesByParameters(int page, int size,
        GiftCertificateSortingParametersDto giftCertificateSortingParameters) {
        var sort = checkIfSortingSet(giftCertificateSortingParameters.getSortBy()).orElse(Sort.unsorted());
        var giftCertificates = giftCertificateRepository.findByParameters(
            giftCertificateSortingParameters.getTagName(), giftCertificateSortingParameters.getPartName(),
            giftCertificateSortingParameters.getPartDescription(), PageRequest.of(page, size, sort));
        return pagedResourcesAssembler.toModel(giftCertificates, giftCertificateDtoAssembler);
    }

    private Optional<Sort> checkIfSortingSet(Map<String, String> sortBy) {
        return sortBy.entrySet()
            .stream()
            .map(entry -> Sort.by(Sort.Direction.fromString(entry.getValue()), entry.getKey()))
            .reduce(Sort::and);
    }

    @Override
    public PagedModel<GiftCertificateDto> getAllGiftCertificatesByTags(int page, int size, TagNamesDto tagNames) {
        var giftCertificates = giftCertificateRepository.findGiftCertificatesByTagNames(tagNames.getTagNames(),
            tagNames.getTagNames().size(), PageRequest.of(page, size));
        return pagedResourcesAssembler.toModel(giftCertificates, giftCertificateDtoAssembler);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public EntityModel<GiftCertificateDto> updateGiftCertificate(String name,
        UpdateGiftCertificateDto updateGiftCertificate) {
        var giftCertificate = getGiftCertificate(name);
        entityDtoMapper.updateGiftCertificateFromUpdateGiftCertificateDto(giftCertificate, updateGiftCertificate);
        giftCertificate.setTags(getExistingTags(updateGiftCertificate.getTags()));
        return EntityModel.of(giftCertificateDtoAssembler.toModel(giftCertificate));
    }

    @Override
    public EntityModel<GiftCertificateDto> deleteGiftCertificate(String name) {
        var giftCertificate = giftCertificateRepository.deleteByName(name);
        if (giftCertificate.isEmpty()) {
            throw new EntityNotFoundException(getErrorMessage(name), HttpErrorCodes.GIFT_CERTIFICATE_NOT_FOUND);
        }
        return EntityModel.of(giftCertificateDtoAssembler.toModel(giftCertificate.get(0)));
    }
}
