package com.carrental.repository.impl;

import com.carrental.model.ContactQuery;
import com.carrental.repository.ContactQueryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcContactQueryRepository implements ContactQueryRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcContactQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<ContactQuery> findAll() {
        String sql = "SELECT id,name,email,message FROM contact_queries";
        return jdbcTemplate.query(sql, new RowMapper<ContactQuery>() {
            @Override
            public ContactQuery mapRow(ResultSet rs, int rowNum) throws SQLException {
                ContactQuery q = new ContactQuery();
                q.setId(rs.getLong("id"));
                q.setName(rs.getString("name"));
                q.setEmail(rs.getString("email"));
                q.setMessage(rs.getString("message"));
                return q;
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM contact_queries WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
