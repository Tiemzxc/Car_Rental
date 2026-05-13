package com.carrental.repository;

import com.carrental.model.Booking;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository {
    Long save(Booking booking);
    List<Booking> findByCustomerId(Long customerId);
    List<Booking> findAll();
    Optional<Booking> findById(Long id);
    boolean hasConflict(Long vehicleId, LocalDate startDate, LocalDate endDate);
}