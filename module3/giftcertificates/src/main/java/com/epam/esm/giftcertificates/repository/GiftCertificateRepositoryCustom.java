package com.epam.esm.giftcertificates.repository;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface GiftCertificateRepositoryCustom {
  Page<GiftCertificate> findByTagsId(Set<Long> tagsId, Pageable pageable);
}
