package com.epam.esm.giftcertificates.service.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.GiftCertificateToTagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GiftCertificateToTagServiceImpl implements GiftCertificateToTagService {

    private final GiftCertificateToTagDao giftCertificateToTagDao;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public void createGiftCertificateToTagLink(GiftCertificateToTagDto giftCertificateToTagDTO) {
        giftCertificateToTagDao.createGiftCertificateToTagLink(entityDtoMapper.giftCertificateToTagDtoToGiftCertificateToTag(giftCertificateToTagDTO));
    }
}
