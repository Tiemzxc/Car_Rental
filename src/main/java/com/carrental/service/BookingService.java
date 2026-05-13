package com.carrental.service;

import com.carrental.model.Booking;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking book(Booking booking);
    List<Booking> listByCustomer(Long customerId);
    List<Booking> listAll();
    Optional<Booking> findById(Long id);
    boolean isConflict(Long vehicleId, LocalDate startDate, LocalDate endDate);
}