package com.carrental.repository.impl;

import com.carrental.model.ContactInfo;
import com.carrental.repository.ContactInfoRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcContactInfoRepository implements ContactInfoRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcContactInfoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<ContactInfo> findById(Long id) {
        String sql = "SELECT id,phone,email,address FROM contact_info WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, rs -> {
            if (rs.next()) {
                ContactInfo info = new ContactInfo();
                info.setId(rs.getLong("id"));
                info.setPhone(rs.getString("phone"));
                info.setEmail(rs.getString("email"));
                info.setAddress(rs.getString("address"));
                return Optional.of(info);
            }
            return Optional.empty();
        });
    }

    @Override
    public void save(ContactInfo info) {
        String sql = "INSERT INTO contact_info (phone,email,address) VALUES (?,?,?)";
        jdbcTemplate.update(sql, info.getPhone(), info.getEmail(), info.getAddress());
    }

    @Override
    public void update(ContactInfo info) {
        String sql = "UPDATE contact_info SET phone=?, email=?, address=? WHERE id=?";
        jdbcTemplate.update(sql, info.getPhone(), info.getEmail(), info.getAddress(), info.getId());
    }
}
