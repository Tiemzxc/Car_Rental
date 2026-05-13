package com.carrental.service;

import com.carrental.model.Booking;
import com.carrental.model.Payment;

import java.math.BigDecimal;

public interface PaymentService {
    Payment processPayment(Booking booking, BigDecimal amount, String method);
    Payment refundPayment(Long paymentId);
    boolean validatePaymentDetails(String cardNumber, String expiry, String cvv);
}