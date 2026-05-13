package com.carrental.service.impl;

import com.carrental.model.Booking;
import com.carrental.model.Payment;
import com.carrental.service.PaymentService;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public Payment processPayment(Booking booking, BigDecimal amount, String method) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setStatus("COMPLETED");
        payment.setTimestamp(LocalDateTime.now());
        return payment;
    }

    @Override
    public Payment refundPayment(Long paymentId) {
        Payment payment = new Payment();
        payment.setId(paymentId);
        payment.setStatus("REFUNDED");
        payment.setTimestamp(LocalDateTime.now());
        return payment;
    }

    @Override
    public boolean validatePaymentDetails(String cardNumber, String expiry, String cvv) {
        return cardNumber != null && cardNumber.matches("\\d{16}") && expiry.matches("(0[1-9]|1[0-2])/\\d{2}") && cvv.matches("\\d{3}");
    }
}