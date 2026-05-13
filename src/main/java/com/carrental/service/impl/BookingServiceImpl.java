package com.carrental.service.impl;

import com.carrental.model.Booking;
import com.carrental.repository.BookingRepository;
import com.carrental.service.BookingService;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public Booking book(Booking booking) {
        booking.setStatus("BOOKED");
        booking.setReturned(false);
        booking.setFine(0d);
        return bookingRepository.findById(bookingRepository.save(booking)).orElse(booking);
    }

    @Override
    public List<Booking> listByCustomer(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }

    @Override
    public List<Booking> listAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public boolean isConflict(Long vehicleId, LocalDate startDate, LocalDate endDate) {
        return bookingRepository.hasConflict(vehicleId, startDate, endDate);
    }
}