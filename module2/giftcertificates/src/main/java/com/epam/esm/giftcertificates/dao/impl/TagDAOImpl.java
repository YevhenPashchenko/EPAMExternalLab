package com.epam.esm.giftcertificates.dao.impl;

import com.epam.esm.giftcertificates.dao.TagDAO;
import com.epam.esm.giftcertificates.entities.Tag;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@AllArgsConstructor
public class TagDAOImpl implements TagDAO {

    private final JdbcTemplate jdbcTemplate;

    private static final String CREATE = "INSERT INTO gc_schema.tag VALUES (DEFAULT, ?)";
    private static final String GET = "SELECT * FROM gc_schema.tag";
    private static final String GET_BY_ID = "SELECT * FROM gc_schema.tag WHERE id = ?";
    private static final String DELETE = "DELETE FROM gc_schema.tag WHERE id = ?";

    public long create(Tag tag) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(CREATE, new String[] {"id"});
            ps.setString(1, tag.getName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    @Override
    public List<Tag> get() {
        return jdbcTemplate.query(GET, new BeanPropertyRowMapper<>(Tag.class));
    }

    @Override
    public Optional<Tag> getById(long id) {
        return jdbcTemplate.query(GET_BY_ID, new Object[] {id}, new int[] {Types.BIGINT}, new BeanPropertyRowMapper<>(Tag.class))
                .stream().findAny();
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE, id);
    }
}
