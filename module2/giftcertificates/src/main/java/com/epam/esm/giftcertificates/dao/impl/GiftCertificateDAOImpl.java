package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.GiftCertificateDAO;
import com.epam.esm.giftcertificates.entities.GiftCertificate;
import com.epam.esm.giftcertificates.entities.dto.TagDTO;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@AllArgsConstructor
public class GiftCertificateDAOImpl implements GiftCertificateDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO gc_schema.gift_certificate VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
    private static final String GET = "SELECT * FROM gc_schema.gift_certificate";
    private static final String GET_BY_ID = "SELECT * FROM gc_schema.gift_certificate WHERE id = ?";
    private static final String UPDATE_NAME = "UPDATE gc_schema.gift_certificate SET name = ?, last_update_date = ? WHERE id = ?";
    private static final String UPDATE_DESCRIPTION = "UPDATE gc_schema.gift_certificate SET description = ?, last_update_date = ? WHERE id = ?";
    private static final String UPDATE_PRICE = "UPDATE gc_schema.gift_certificate SET price = ?, last_update_date = ? WHERE id = ?";
    private static final String UPDATE_DURATION = "UPDATE gc_schema.gift_certificate SET duration = ?, last_update_date = ? WHERE id = ?";
    private static final String DELETE = "DELETE FROM gc_schema.gift_certificate WHERE id = ?";

    @Override
    public long create(GiftCertificate giftCertificate) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE, new String[] {"id"});
            ps.setString(1, giftCertificate.getName());
            ps.setString(2, giftCertificate.getDescription());
            ps.setBigDecimal(3, giftCertificate.getPrice());
            ps.setInt(4, giftCertificate.getDuration());
            ps.setObject(5, giftCertificate.getCreateDate());
            ps.setObject(6, giftCertificate.getLastUpdateDate());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<GiftCertificate> get() {
        return jdbcTemplate.query(GET, new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    @Override
    public Optional<GiftCertificate> getById(long id) {
        return jdbcTemplate.query(GET_BY_ID, new Object[] {id}, new int[] {Types.INTEGER},
                        new BeanPropertyRowMapper<>(GiftCertificate.class)).stream().findAny();
    }

    @Override
    public List<GiftCertificate> getByParameters(GiftCertificate giftCertificate, List<TagDTO> tagDTOs, Map<String, String> sortingParameters) {
        StringBuilder query = new StringBuilder("SELECT gift_certificate.* FROM gc_schema.gift_certificate\n");
        List<Object> parameters = new ArrayList<>();
        List<Integer> parametersTypes = new ArrayList<>();
        addSortingByTagNameToQuery(tagDTOs, query, parameters, parametersTypes);
        addSortingByNameOrDescriptionToQuery(giftCertificate, query, parameters, parametersTypes);
        addOrderingToQuery(sortingParameters, query);
        int[] parametersTypesArray = new int[parametersTypes.size()];
        for (int i = 0; i < parametersTypes.size(); i++) {
            parametersTypesArray[i] = parametersTypes.get(i);
        }
        return jdbcTemplate.query(query.toString(), parameters.toArray(), parametersTypesArray,
                new BeanPropertyRowMapper<>(GiftCertificate.class));
    }

    private void addSortingByTagNameToQuery(List<TagDTO> tagDTOs, StringBuilder query, List<Object> parameters, List<Integer> parametersTypes) {
        if (!tagDTOs.isEmpty()) {
            query.append("INNER JOIN gc_schema.gc_tag ON gift_certificate.id = gc_tag.gc_id\n");
            query.append("INNER JOIN gc_schema.tag ON gc_tag.tag_id = tag.id\n");
            query.append("WHERE tag.name = ?");
            parameters.add(tagDTOs.get(0).getName());
            parametersTypes.add(Types.VARCHAR);
        }
    }

    private void addSortingByNameOrDescriptionToQuery(GiftCertificate giftCertificate, StringBuilder query, List<Object> parameters, List<Integer> parametersTypes) {
        if (giftCertificate.getName() != null) {
            if (parameters.isEmpty()) {
                query.append("WHERE ");
            } else {
                query.append(" AND ");
            }
            query.append("gift_certificate.name LIKE ?");
            parameters.add(giftCertificate.getName() + "%");
            parametersTypes.add(Types.VARCHAR);
        } else if (giftCertificate.getDescription() != null) {
            if (parameters.isEmpty()) {
                query.append("WHERE ");
            } else {
                query.append(" AND ");
            }
            query.append("gift_certificate.description LIKE ?");
            parameters.add(giftCertificate.getDescription() + "%");
            parametersTypes.add(Types.VARCHAR);
        }
    }

    private void addOrderingToQuery(Map<String, String> sortingParameters, StringBuilder query) {
        if (!sortingParameters.isEmpty()) {
            AtomicInteger count = new AtomicInteger();
            query.append("ORDER BY ");
            sortingParameters.forEach((key, value) -> {
                if (count.get() == 0) {
                    query.append("gift_certificate.");
                } else {
                    query.append(", gift_certificate.");
                }
                query.append(key).append(" ").append(value);
                count.getAndIncrement();
            });
        }
    }

    @Override
    public void updateName(long id, String name, LocalDateTime lastUpdateDate) {
        jdbcTemplate.update(UPDATE_NAME, name, lastUpdateDate, id);
    }

    @Override
    public void updateDescription(long id, String description, LocalDateTime lastUpdateDate) {
        jdbcTemplate.update(UPDATE_DESCRIPTION, description, lastUpdateDate, id);
    }

    @Override
    public void updatePrice(long id, BigDecimal price, LocalDateTime lastUpdateDate) {
        jdbcTemplate.update(UPDATE_PRICE, price, lastUpdateDate, id);
    }

    @Override
    public void updateDuration(long id, int duration, LocalDateTime lastUpdateDate) {
        jdbcTemplate.update(UPDATE_DURATION, duration, lastUpdateDate, id);
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE, id);
    }
}
