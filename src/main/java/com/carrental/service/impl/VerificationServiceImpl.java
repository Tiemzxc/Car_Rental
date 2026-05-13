package com.carrental.service.impl;

import com.carrental.model.OtpVerification;
import com.carrental.service.VerificationService;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class VerificationServiceImpl implements VerificationService {
    private final Map<String, OtpVerification> otpStore = new ConcurrentHashMap<>();
    private final JavaMailSender mailSender;

    public VerificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public OtpVerification generateOtp(String email) {
        OtpVerification otp = new OtpVerification();
        otp.setEmail(email);
        String code = String.format("%06d", (int) (Math.random() * 900000 + 100000));
        otp.setOtpCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);
        otpStore.put(email, otp);
        return otp;
    }

    @Override
    public boolean verifyOtp(String email, String otpCode) {
        if (!otpStore.containsKey(email)) {
            return false;
        }
        OtpVerification otp = otpStore.get(email);
        if (otp.isVerified()) {
            return true;
        }
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            return false;
        }
        if (otp.getOtpCode().equals(otpCode)) {
            otp.setVerified(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean isOtpValid(String email, String otpCode) {
        OtpVerification otp = otpStore.get(email);
        return otp != null && !otp.isVerified() && otp.getExpiresAt().isAfter(LocalDateTime.now()) && otp.getOtpCode().equals(otpCode);
    }

    @Override
    public void sendOtpEmail(String email, String otpCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("CarRental OTP Verification");
        message.setText("Your OTP code is: " + otpCode + "\nIt expires in 5 minutes.");
        try {
            mailSender.send(message);
        } catch (org.springframework.mail.MailException ex) {
            // Log and propagate to controller for user-friendly message
            System.err.println("[OTP EMAIL ERROR] " + ex.getMessage());
            throw ex;
        }
    }
}