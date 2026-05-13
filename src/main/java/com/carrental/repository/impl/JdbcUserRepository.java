package com.carrental.repository.impl;

import com.carrental.model.Admin;
import com.carrental.model.Customer;
import com.carrental.model.User;
import com.carrental.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long saveUser(User user) {
        String sql = "INSERT INTO users (first_name,middle_name,last_name,email,password,contact_number,role,driving_license) VALUES (?,?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getFirstName(), user.getMiddleName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getContactNumber(), user.getRole(), user instanceof Customer ? ((Customer) user).getDrivingLicense() : null);
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> list = jdbcTemplate.query(sql, new Object[]{email}, new UserRowMapper());
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            String role = rs.getString("role");
            User user = "ADMIN".equalsIgnoreCase(role) ? new Admin() : new Customer();
            user.setId(rs.getLong("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setMiddleName(rs.getString("middle_name"));
            user.setLastName(rs.getString("last_name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setContactNumber(rs.getString("contact_number"));
            user.setRole(role);
            if (user instanceof Customer) {
                ((Customer) user).setDrivingLicense(rs.getString("driving_license"));
            }
            return user;
        }
    }
}
