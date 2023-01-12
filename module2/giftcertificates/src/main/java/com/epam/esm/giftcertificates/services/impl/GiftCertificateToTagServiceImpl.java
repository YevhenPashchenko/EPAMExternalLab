package com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.mappers.EntityDtoMapper;
import com.epam.esm.giftcertificates.services.GiftCertificateToTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftCertificateToTagServiceImpl implements GiftCertificateToTagService {

    private final GiftCertificateToTagDao giftCertificateToTagDao;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public void create(GiftCertificateToTagDto giftCertificateToTagDTO) {
        giftCertificateToTagDao.create(entityDtoMapper.giftCertificateToTagDtoToGiftCertificateToTag(giftCertificateToTagDTO));
    }
}
