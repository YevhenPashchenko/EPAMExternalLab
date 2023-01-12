package com.epam.esm.giftcertificate.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.entities.GiftCertificateToTag;
import com.epam.esm.giftcertificates.mappers.EntityDtoMapper;
import com.epam.esm.giftcertificates.services.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.services.impl.GiftCertificateToTagServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class GiftCertificateToTagServiceImplTest {

    private final GiftCertificateToTagDao giftCertificateToTagDao = Mockito.mock(GiftCertificateToTagDao.class);
    private final EntityDtoMapper entityDtoMapper = Mockito.mock(EntityDtoMapper.class);
    private final GiftCertificateToTagService giftCertificateToTagService =
            new GiftCertificateToTagServiceImpl(giftCertificateToTagDao, entityDtoMapper);

    @Test
    void create_whenExecute_giftCertificateToTagDaoCreateCalls() {
        //GIVEN
        given(entityDtoMapper.giftCertificateToTagDtoToGiftCertificateToTag(new GiftCertificateToTagDto()))
                .willReturn(new GiftCertificateToTag());

        //WHEN
        giftCertificateToTagService.create(new GiftCertificateToTagDto());

        //THEN
        then(giftCertificateToTagDao).should(times(1)).create(new GiftCertificateToTag());
    }
}
