package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.assembler.GiftCertificateDtoAssembler;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepository;
import com.epam.esm.giftcertificates.dto.GiftCertificateDto;
import com.epam.esm.giftcertificates.dto.GiftCertificateDtoForUpdate;
import com.epam.esm.giftcertificates.dto.GiftCertificateSortingParametersDto;
import com.epam.esm.giftcertificates.dto.TagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.handler.exception.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
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

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

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
  public EntityModel<GiftCertificateDto> createGiftCertificate(
      GiftCertificateDto giftCertificateDto) {
    getEachTagByName(giftCertificateDto);
    var giftCertificate = entityDtoMapper.giftCertificateDtoToGiftCertificate(giftCertificateDto);
    giftCertificateRepository.save(giftCertificate);
    return EntityModel.of(giftCertificateDtoAssembler.toModel(giftCertificate));
  }

  @Override
  public PagedModel<GiftCertificateDto> getAllGiftCertificateDto(int page, int size) {
    return pagedResourcesAssembler.toModel(
        giftCertificateRepository.findAll(PageRequest.of(page, size)), giftCertificateDtoAssembler);
  }

  @Override
  public EntityModel<GiftCertificateDto> getGiftCertificateDtoById(Long id) {
    return EntityModel.of(giftCertificateDtoAssembler.toModel(getGiftCertificateById(id)));
  }

  @Override
  public GiftCertificate getGiftCertificateById(Long id) {
    return giftCertificateRepository
        .findById(id)
        .orElseThrow(() -> new GiftCertificateNotFoundException(id));
  }

  @Override
  public PagedModel<GiftCertificateDto> getAllGiftCertificateDtoByParameters(
      int page, int size, GiftCertificateSortingParametersDto giftCertificateSortingParametersDto) {
    var sort =
        checkIfSortingSet(giftCertificateSortingParametersDto.getSortBy()).orElse(Sort.unsorted());
    var listOfGiftCertificate =
        giftCertificateRepository.findByParameters(
            giftCertificateSortingParametersDto.getTagName(),
            giftCertificateSortingParametersDto.getPartName(),
            giftCertificateSortingParametersDto.getPartDescription(),
            PageRequest.of(page, size, sort));
    listOfGiftCertificate.forEach(
        giftCertificate ->
            giftCertificate.setTags(
                tagService.getTagsByGiftCertificateId(giftCertificate.getId())));
    return pagedResourcesAssembler.toModel(listOfGiftCertificate, giftCertificateDtoAssembler);
  }

  @Override
  @Transactional(isolation = Isolation.REPEATABLE_READ)
  public EntityModel<GiftCertificateDto> updateGiftCertificate(
      GiftCertificateDtoForUpdate giftCertificateDtoForUpdate) {
    var giftCertificate = getGiftCertificateById(giftCertificateDtoForUpdate.getId());
    entityDtoMapper.updateGiftCertificateFromGiftCertificateDto(
        giftCertificate, giftCertificateDtoForUpdate);
    return EntityModel.of(giftCertificateDtoAssembler.toModel(giftCertificate));
  }

  @Override
  public void deleteGiftCertificate(Long id) {
    giftCertificateRepository.deleteById(id);
  }

  private void getEachTagByName(GiftCertificateDto giftCertificateDto) {
    var listOfTagDto = new ArrayList<TagDto>();
    giftCertificateDto
        .getTags()
        .forEach(tagDto -> listOfTagDto.add(tagService.getTagDtoByName(tagDto.getName())));
    giftCertificateDto.setTags(listOfTagDto);
  }

  private Optional<Sort> checkIfSortingSet(Map<String, String> sortBy) {
    return sortBy.entrySet().stream()
        .map(entry -> Sort.by(Sort.Direction.fromString(entry.getValue()), entry.getKey()))
        .reduce(Sort::and);
  }
}
