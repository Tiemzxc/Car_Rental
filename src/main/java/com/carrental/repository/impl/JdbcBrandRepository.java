package com.carrental.repository.impl;

import com.carrental.model.Brand;
import com.carrental.repository.BrandRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBrandRepository implements BrandRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcBrandRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Brand brand) {
        String sql = "INSERT INTO brands (name) VALUES (?)";
        jdbcTemplate.update(sql, brand.getName());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public Optional<Brand> findById(Long id) {
        String sql = "SELECT id,name FROM brands WHERE id = ?";
        List<Brand> list = jdbcTemplate.query(sql, new Object[]{id}, new BrandRowMapper());
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(list.get(0));
    }

    @Override
    public List<Brand> findAll() {
        String sql = "SELECT id,name FROM brands";
        return jdbcTemplate.query(sql, new BrandRowMapper());
    }

    @Override
    public void update(Brand brand) {
        String sql = "UPDATE brands SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, brand.getName(), brand.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM brands WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private static class BrandRowMapper implements RowMapper<Brand> {
        @Override
        public Brand mapRow(ResultSet rs, int rowNum) throws SQLException {
            Brand brand = new Brand();
            brand.setId(rs.getLong("id"));
            brand.setName(rs.getString("name"));
            return brand;
        }
    }
}
