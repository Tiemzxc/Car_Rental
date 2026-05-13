package com.carrental.service;

import com.carrental.model.OtpVerification;

public interface VerificationService {
    OtpVerification generateOtp(String email);
    boolean verifyOtp(String email, String otpCode);
    boolean isOtpValid(String email, String otpCode);
    void sendOtpEmail(String email, String otpCode);
}