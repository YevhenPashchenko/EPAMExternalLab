package unit.com.epam.esm.giftcertificates.services.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateToTagDao;
import com.epam.esm.giftcertificates.dto.GiftCertificateToTagDto;
import com.epam.esm.giftcertificates.entity.GiftCertificateToTag;
import com.epam.esm.giftcertificates.mapper.EntityDtoMapper;
import com.epam.esm.giftcertificates.service.GiftCertificateToTagService;
import com.epam.esm.giftcertificates.service.impl.GiftCertificateToTagServiceImpl;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class GiftCertificateToTagServiceImplTest {

    private final GiftCertificateToTagDao giftCertificateToTagDao = mock(GiftCertificateToTagDao.class);
    private final EntityDtoMapper entityDtoMapper = mock(EntityDtoMapper.class);
    private final GiftCertificateToTagService giftCertificateToTagService =
            new GiftCertificateToTagServiceImpl(giftCertificateToTagDao, entityDtoMapper);

    @Test
    void createGiftCertificateToTagLink_shouldCallsGiftCertificateToTagDaoCreateGiftCertificateToTagLink_whenExecuteNormally() {
        //GIVEN
        given(entityDtoMapper.giftCertificateToTagDtoToGiftCertificateToTag(any(GiftCertificateToTagDto.class)))
                .willReturn(new GiftCertificateToTag());

        //WHEN
        giftCertificateToTagService.createGiftCertificateToTagLink(new GiftCertificateToTagDto());

        //THEN
        then(giftCertificateToTagDao).should(times(1)).createGiftCertificateToTagLink(any(GiftCertificateToTag.class));
    }
}
