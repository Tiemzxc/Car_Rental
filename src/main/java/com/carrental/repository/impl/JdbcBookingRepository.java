package com.carrental.repository.impl;

import com.carrental.model.Booking;
import com.carrental.model.Customer;
import com.carrental.model.Vehicle;
import com.carrental.repository.BookingRepository;
import com.carrental.repository.UserRepository;
import com.carrental.repository.VehicleRepository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBookingRepository implements BookingRepository {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public JdbcBookingRepository(JdbcTemplate jdbcTemplate, UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Long save(Booking booking) {
        String sql = "INSERT INTO bookings (customer_id,vehicle_id,start_date,end_date,returned,fine,status) VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, booking.getCustomer().getId(), booking.getVehicle().getId(), booking.getStartDate(), booking.getEndDate(), booking.isReturned(), booking.getFine(), booking.getStatus());
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
    }

    @Override
    public List<Booking> findByCustomerId(Long customerId) {
        String sql = "SELECT b.id,b.customer_id,b.vehicle_id,b.start_date,b.end_date,b.returned,b.fine,b.status,v.id as vid,v.model,v.type,v.image_url,v.price_per_day,v.available,bn.id as bid,bn.name as brand_name FROM bookings b JOIN vehicles v ON b.vehicle_id=v.id JOIN brands bn ON v.brand_id=bn.id WHERE b.customer_id=?";
        return jdbcTemplate.query(sql, new Object[]{customerId}, new BookingRowMapper());
    }

    @Override
    public List<Booking> findAll() {
        String sql = "SELECT b.id,b.customer_id,b.vehicle_id,b.start_date,b.end_date,b.returned,b.fine,b.status,v.id as vid,v.model,v.type,v.image_url,v.price_per_day,v.available,bn.id as bid,bn.name as brand_name FROM bookings b JOIN vehicles v ON b.vehicle_id=v.id JOIN brands bn ON v.brand_id=bn.id";
        return jdbcTemplate.query(sql, new BookingRowMapper());
    }

    @Override
    public Optional<Booking> findById(Long id) {
        String sql = "SELECT b.id,b.customer_id,b.vehicle_id,b.start_date,b.end_date,b.returned,b.fine,b.status,v.id as vid,v.model,v.type,v.image_url,v.price_per_day,v.available,bn.id as bid,bn.name as brand_name FROM bookings b JOIN vehicles v ON b.vehicle_id=v.id JOIN brands bn ON v.brand_id=bn.id WHERE b.id=?";
        List<Booking> list = jdbcTemplate.query(sql, new Object[]{id}, new BookingRowMapper());
        if (list.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(list.get(0));
    }

    @Override
    public boolean hasConflict(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT count(*) FROM bookings WHERE vehicle_id=? AND status IN ('BOOKED','ONGOING') AND NOT (end_date < ? OR start_date > ? )";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, vehicleId, startDate, endDate);
        return count != null && count > 0;
    }

    private class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(ResultSet rs, int rowNum) throws SQLException {
            Booking booking = new Booking();
            booking.setId(rs.getLong("id"));
            Long custId = rs.getLong("customer_id");
            Customer customer = new Customer();
            customer.setId(custId);
            booking.setCustomer(customer);
            Vehicle vehicle = new Vehicle();
            vehicle.setId(rs.getLong("vid"));
            vehicle.setModel(rs.getString("model"));
            vehicle.setType(rs.getString("type"));
            vehicle.setImageUrl(rs.getString("image_url"));
            vehicle.setPricePerDay(rs.getBigDecimal("price_per_day"));
            vehicle.setAvailable(rs.getBoolean("available"));
            booking.setVehicle(vehicle);
            booking.setStartDate(rs.getDate("start_date").toLocalDate());
            booking.setEndDate(rs.getDate("end_date").toLocalDate());
            booking.setReturned(rs.getBoolean("returned"));
            booking.setFine(rs.getDouble("fine"));
            booking.setStatus(rs.getString("status"));
            return booking;
        }
    }
}
