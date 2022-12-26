package com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDAO;
import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;
import com.epam.esm.giftcertificates.entities.dto.GiftCertificateToTagDTO;
import com.epam.esm.giftcertificates.services.GiftCertificateToTagService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class GiftCertificateToTagServiceImpl implements GiftCertificateToTagService {

    private final GiftCertificateToTagDAO giftCertificateToTagDAO;
    private final ModelMapper modelMapper;

    @Override
    public void create(GiftCertificateToTagDTO giftCertificateToTagDTO) {
        giftCertificateToTagDAO.create(convertToEntity(giftCertificateToTagDTO));
    }

    private GiftCertificateToTag convertToEntity(GiftCertificateToTagDTO giftCertificateToTagDTO) {
        return modelMapper.map(giftCertificateToTagDTO, GiftCertificateToTag.class);
    }
}
