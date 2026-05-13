package com.carrental.repository.impl;

import com.carrental.model.Brand;
import com.carrental.model.Vehicle;
import com.carrental.repository.VehicleRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcVehicleRepository implements VehicleRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcVehicleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (model,type,brand_id,image_url,price_per_day,available) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, vehicle.getModel(), vehicle.getType(), vehicle.getBrand().getId(), vehicle.getImageUrl(), vehicle.getPricePerDay(), vehicle.isAvailable());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public List<Vehicle> findAll() {
        String sql = "SELECT v.id,v.model,v.type,v.image_url,v.price_per_day,v.available,b.id brand_id,b.name brand_name FROM vehicles v JOIN brands b ON v.brand_id=b.id";
        return jdbcTemplate.query(sql, new VehicleRowMapper());
    }

    @Override
    public Optional<Vehicle> findById(Long id) {
        String sql = "SELECT v.id,v.model,v.type,v.image_url,v.price_per_day,v.available,b.id brand_id,b.name brand_name FROM vehicles v JOIN brands b ON v.brand_id=b.id WHERE v.id = ?";
        List<Vehicle> list = jdbcTemplate.query(sql, new Object[]{id}, new VehicleRowMapper());
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public void update(Vehicle vehicle) {
        String sql = "UPDATE vehicles SET model=?,type=?,brand_id=?,image_url=?,price_per_day=?,available=? WHERE id=?";
        jdbcTemplate.update(sql, vehicle.getModel(), vehicle.getType(), vehicle.getBrand().getId(), vehicle.getImageUrl(), vehicle.getPricePerDay(), vehicle.isAvailable(), vehicle.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM vehicles WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    private static class VehicleRowMapper implements RowMapper<Vehicle> {
        @Override
        public Vehicle mapRow(ResultSet rs, int rowNum) throws SQLException {
            Vehicle vehicle = new Vehicle();
            vehicle.setId(rs.getLong("id"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setType(rs.getString("type"));
            Brand brand = new Brand();
            brand.setId(rs.getLong("brand_id"));
            brand.setName(rs.getString("brand_name"));
            vehicle.setBrand(brand);
            vehicle.setImageUrl(rs.getString("image_url"));
            vehicle.setPricePerDay(rs.getBigDecimal("price_per_day"));
            vehicle.setAvailable(rs.getBoolean("available"));
            return vehicle;
        }
    }
}
