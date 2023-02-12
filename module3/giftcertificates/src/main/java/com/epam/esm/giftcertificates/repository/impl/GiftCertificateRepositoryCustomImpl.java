package com.epam.esm.giftcertificates.repository.impl;

import com.epam.esm.giftcertificates.entity.GiftCertificate;
import com.epam.esm.giftcertificates.repository.GiftCertificateRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public class GiftCertificateRepositoryCustomImpl implements GiftCertificateRepositoryCustom {

  @PersistenceContext private EntityManager entityManager;

  @Override
  public Page<GiftCertificate> findByTagsId(Set<Long> tagsId, Pageable pageable) {
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    var criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
    var giftCertificate = criteriaQuery.from(GiftCertificate.class);
    var joinTags = giftCertificate.join("tags", JoinType.LEFT);
    criteriaQuery
        .select(giftCertificate)
        .where(joinTags.get("id").in(tagsId))
        .groupBy(giftCertificate.get("id"))
        .having(
            criteriaBuilder.equal(criteriaBuilder.count(giftCertificate.get("id")), tagsId.size()));
    var typedQuery = entityManager.createQuery(criteriaQuery);
    typedQuery.setFirstResult(pageable.getPageNumber());
    typedQuery.setMaxResults(pageable.getPageSize());
    return new PageImpl<>(typedQuery.getResultList(), pageable, getCount(tagsId));
  }

  private Long getCount(Set<Long> tagsId) {
    var criteriaBuilder = entityManager.getCriteriaBuilder();
    var criteriaQuery = criteriaBuilder.createQuery(Long.class);
    var giftCertificate = criteriaQuery.from(GiftCertificate.class);
    var joinTags = giftCertificate.join("tags", JoinType.LEFT);
    criteriaQuery
        .select(criteriaBuilder.count(giftCertificate.get("id")))
        .where(joinTags.get("id").in(tagsId))
        .groupBy(giftCertificate.get("id"))
        .having(
            criteriaBuilder.equal(criteriaBuilder.count(giftCertificate.get("id")), tagsId.size()));
    return (long) entityManager.createQuery(criteriaQuery).getResultList().size();
  }
}
