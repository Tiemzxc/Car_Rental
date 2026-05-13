package com.carrental.repository.impl;

import com.carrental.model.Subscriber;
import com.carrental.repository.SubscriberRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcSubscriberRepository implements SubscriberRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcSubscriberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Subscriber> findAll() {
        String sql = "SELECT id,email FROM subscribers";
        return jdbcTemplate.query(sql, new RowMapper<Subscriber>() {
            @Override
            public Subscriber mapRow(ResultSet rs, int rowNum) throws SQLException {
                Subscriber s = new Subscriber();
                s.setId(rs.getLong("id"));
                s.setEmail(rs.getString("email"));
                return s;
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM subscribers WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
