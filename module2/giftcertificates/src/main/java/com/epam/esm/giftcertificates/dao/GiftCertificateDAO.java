package com.epam.esm.giftcertificates.dao;

import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.dto.TagDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GiftCertificateDAO {

    long create(GiftCertificate giftCertificate);

    List<GiftCertificate> get();

    Optional<GiftCertificate> getById(long id);

    List<GiftCertificate> getByParameters(GiftCertificate giftCertificate, List<TagDTO> tagDTOs, Map<String, String> sortingParameters);

    void updateName(long id, String name, LocalDateTime lastUpdateDate);

    void updateDescription(long id, String description, LocalDateTime lastUpdateDate);

    void updatePrice(long id, BigDecimal price, LocalDateTime lastUpdateDate);

    void updateDuration(long id, int duration, LocalDateTime lastUpdateDate);

    void delete(long id);
}
