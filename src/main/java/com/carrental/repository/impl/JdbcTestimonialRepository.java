package com.carrental.repository.impl;

import com.carrental.model.Testimonial;
import com.carrental.repository.TestimonialRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTestimonialRepository implements TestimonialRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTestimonialRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long save(Testimonial t) {
        String sql = "INSERT INTO testimonials (customer_id,message,rating,status) VALUES (?,?,?,?)";
        jdbcTemplate.update(sql, t.getCustomer().getId(), t.getMessage(), t.getRating(), t.getStatus());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public List<Testimonial> findAll() {
        String sql = "SELECT t.id,t.message,t.rating,t.status,u.id user_id,u.first_name,u.last_name FROM testimonials t JOIN users u ON t.customer_id=u.id";
        return jdbcTemplate.query(sql, new TestimonialRowMapper());
    }

    @Override
    public Optional<Testimonial> findById(Long id) {
        String sql = "SELECT t.id,t.message,t.rating,t.status,u.id user_id,u.first_name,u.last_name FROM testimonials t JOIN users u ON t.customer_id=u.id WHERE t.id = ?";
        List<Testimonial> list = jdbcTemplate.query(sql, new Object[]{id}, new TestimonialRowMapper());
        return list.stream().findFirst();
    }

    @Override
    public void updateStatus(Long id, String status) {
        String sql = "UPDATE testimonials SET status=? WHERE id=?";
        jdbcTemplate.update(sql, status, id);
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM testimonials WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    private static class TestimonialRowMapper implements RowMapper<Testimonial> {
        @Override
        public Testimonial mapRow(ResultSet rs, int rowNum) throws SQLException {
            Testimonial t = new Testimonial();
            t.setId(rs.getLong("id"));
            t.setMessage(rs.getString("message"));
            t.setRating(rs.getInt("rating"));
            t.setStatus(rs.getString("status"));
            com.carrental.model.Customer c = new com.carrental.model.Customer();
            c.setId(rs.getLong("user_id"));
            c.setFirstName(rs.getString("first_name"));
            c.setLastName(rs.getString("last_name"));
            t.setCustomer(c);
            return t;
        }
    }
}
