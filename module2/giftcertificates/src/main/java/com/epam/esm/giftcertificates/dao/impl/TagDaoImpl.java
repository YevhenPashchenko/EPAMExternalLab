package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.TagDao;
import com.epam.esm.giftcertificates.constant.TagDaoConstants;
import com.epam.esm.giftcertificates.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TagDaoImpl implements TagDao {

    private final JdbcTemplate jdbcTemplate;

    public long createTag(Tag tag) {
        var keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(TagDaoConstants.CREATE_TAG_QUERY, new String[] {"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Tag> getListOfTag() {
        return jdbcTemplate.query(TagDaoConstants.SELECT_ALL_TAGS_QUERY, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> getTagById(long id) {
        return jdbcTemplate.query(TagDaoConstants.SELECT_TAG_BY_ID_QUERY, new Object[] {id}, new int[] {Types.BIGINT},
                        new BeanPropertyRowMapper<>(Tag.class)).stream().findAny();
    }

    @Override
    public List<Tag> getListOfTagByGiftCertificateId(long id) {
        return jdbcTemplate.query(TagDaoConstants.SELECT_TAGS_BY_GIFT_CERTIFICATE_ID_QUERY, new Object[] {id},
                new int[] {Types.BIGINT}, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public void deleteTag(long id) {
        jdbcTemplate.update(TagDaoConstants.DELETE_TAG_QUERY, id);
    }
}
