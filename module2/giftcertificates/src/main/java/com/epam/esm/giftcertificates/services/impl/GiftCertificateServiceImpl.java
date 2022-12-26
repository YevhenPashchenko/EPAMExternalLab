package com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDAO;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.dto.GiftCertificateDTO;
import com.epam.esm.giftcertificates.entities.dto.GiftCertificateToTagDTO;
import com.epam.esm.giftcertificates.handlers.exceptions.GiftCertificateNotFoundException;
import com.epam.esm.giftcertificates.services.GiftCertificateService;
import com.epam.esm.giftcertificates.services.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.services.TagService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class GiftCertificateServiceImpl implements GiftCertificateService {

    private final GiftCertificateDAO giftCertificateDAO;
    private final TagService tagService;
    private final GiftCertificateToTagService giftCertificateToTagService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(GiftCertificateDTO giftCertificateDTO) {
        LocalDateTime now = LocalDateTime.now();
        giftCertificateDTO.setCreateDate(now);
        giftCertificateDTO.setLastUpdateDate(now);

        long id = giftCertificateDAO.create(convertToEntity(giftCertificateDTO));

        if (!giftCertificateDTO.getTags().isEmpty()) {
            giftCertificateDTO.getTags().stream().filter(tagDTO -> tagDTO.getId() == 0)
                    .forEach(tagDTO -> tagDTO.setId(tagService.create(tagDTO)));

            giftCertificateDTO.getTags().forEach(tagDTO -> {
                GiftCertificateToTagDTO giftCertificateToTagDTO = new GiftCertificateToTagDTO();
                giftCertificateToTagDTO.setGiftCertificateId(id);
                giftCertificateToTagDTO.setTagId(tagDTO.getId());
                giftCertificateToTagService.create(giftCertificateToTagDTO);
            });
        }
    }

    @Override
    public List<GiftCertificateDTO> get() {
        List<GiftCertificateDTO> giftCertificateDTOs = new ArrayList<>();
        List<GiftCertificate> giftCertificates = giftCertificateDAO.get();
        giftCertificates.forEach(giftCertificate -> giftCertificateDTOs.add(convertToDTO(giftCertificate)));
        return giftCertificateDTOs;
    }

    @Override
    public GiftCertificateDTO getById(long id) {
        return convertToDTO(giftCertificateDAO.getById(id).orElseThrow(() -> new GiftCertificateNotFoundException(id)));
    }

    @Override
    public List<GiftCertificateDTO> getByParameters(GiftCertificateDTO giftCertificateDTO) {
        Map<String, String> sortingParameters = new HashMap<>();
        checkSortingParameters(giftCertificateDTO, sortingParameters);
        List<GiftCertificate> giftCertificates = giftCertificateDAO.getByParameters(convertToEntity(giftCertificateDTO),
                giftCertificateDTO.getTags(), sortingParameters);
        List<GiftCertificateDTO> giftCertificateDTOs = new ArrayList<>();
        giftCertificates.forEach(giftCertificate -> giftCertificateDTOs.add(convertToDTO(giftCertificate)));
        return giftCertificateDTOs;
    }

    private void checkSortingParameters(GiftCertificateDTO giftCertificateDTO, Map<String, String> sortingParameters) {
        if (giftCertificateDTO.getSortByName() != null) {
            if (giftCertificateDTO.getSortByName().equals("ASC")) {
                sortingParameters.put("name", "ASC");
            } else {
                sortingParameters.put("name", "DESC");
            }
        }
        if (giftCertificateDTO.getSortByCreateDate() != null) {
            if (giftCertificateDTO.getSortByCreateDate().equals("asc")) {
                sortingParameters.put("create_date", "ASC");
            } else {
                sortingParameters.put("create_date", "DESC");
            }
        } else if (giftCertificateDTO.getSortByLastUpdateDate() != null) {
            if (giftCertificateDTO.getSortByLastUpdateDate().equals("asc")) {
                sortingParameters.put("last_update_date", "ASC");
            } else {
                sortingParameters.put("last_update_date", "DESC");
            }
        }
    }

    @Override
    @Transactional
    public void update(GiftCertificateDTO giftCertificateDTO) {
        GiftCertificate giftCertificate = convertToEntity(giftCertificateDTO);
        LocalDateTime now = LocalDateTime.now();
        giftCertificate.setLastUpdateDate(now);

        if (giftCertificate.getName() != null) {
            giftCertificateDAO.updateName(giftCertificate.getId(), giftCertificate.getName(), giftCertificate.getLastUpdateDate());
        }

        if (giftCertificate.getDescription() != null) {
            giftCertificateDAO.updateDescription(giftCertificate.getId(), giftCertificate.getDescription(), giftCertificate.getLastUpdateDate());
        }

        if (giftCertificate.getPrice() != null) {
            giftCertificateDAO.updatePrice(giftCertificate.getId(), giftCertificate.getPrice(), giftCertificate.getLastUpdateDate());
        }

        if (giftCertificate.getDuration() != 0) {
            giftCertificateDAO.updateDuration(giftCertificate.getId(), giftCertificate.getDuration(), giftCertificate.getLastUpdateDate());
        }
    }

    @Override
    public void delete(long id) {
        giftCertificateDAO.delete(id);
    }

    private GiftCertificate convertToEntity(GiftCertificateDTO giftCertificateDTO) {
        return modelMapper.map(giftCertificateDTO, GiftCertificate.class);
    }

    private GiftCertificateDTO convertToDTO(GiftCertificate giftCertificate) {
        return modelMapper.map(giftCertificate, GiftCertificateDTO.class);
    }
}
