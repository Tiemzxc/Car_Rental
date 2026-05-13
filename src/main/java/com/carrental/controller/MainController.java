package com.carrental.controller;

import com.carrental.model.Booking;
import com.carrental.model.Customer;
import com.carrental.model.OtpVerification;
import com.carrental.model.Testimonial;
import com.carrental.model.User;
import com.carrental.model.Vehicle;
import com.carrental.service.BookingService;
import com.carrental.service.UserService;
import com.carrental.service.VerificationService;
import com.carrental.service.VehicleService;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private final UserService userService;
    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final VerificationService verificationService;

    public MainController(UserService userService, VehicleService vehicleService, BookingService bookingService, VerificationService verificationService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
        this.verificationService = verificationService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Vehicle> vehicles = vehicleService.listAll();
        model.addAttribute("vehicles", vehicles);
        return "index";
    }

    @PostMapping("/register")
    public String register(@RequestParam String firstName,
                           @RequestParam(required = false) String middleName,
                           @RequestParam String lastName,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String contactNumber,
                           @RequestParam String drivingLicense,
                           Model model) {
        if (!firstName.matches("[A-Za-z]+") || !lastName.matches("[A-Za-z]+")) {
            model.addAttribute("error", "Name must contain letters only");
            return "index";
        }
        if (!contactNumber.matches("\\d{11}")) {
            model.addAttribute("error", "Contact number must be 11 digits");
            return "index";
        }
        if (!email.matches("^[^@]+@[^@]+\\.[^@]+$")) {
            model.addAttribute("error", "Email is invalid");
            return "index";
        }
        if (!password.matches("(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z]).{8,}")) {
            model.addAttribute("error", "Password must be minimum 8 chars with upper/lower/digit");
            return "index";
        }

        if (userService.getAllUsers().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email))) {
            model.addAttribute("error", "Email already registered");
            return "index";
        }

        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setMiddleName(middleName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setContactNumber(contactNumber);
        customer.setDrivingLicense(drivingLicense);
        customer.setRole("CUSTOMER");
        try {
            userService.register(customer);
        } catch (org.springframework.dao.DuplicateKeyException ex) {
            model.addAttribute("error", "Email already registered. Please login or use another email.");
            return "index";
        } catch (org.springframework.dao.DataAccessException ex) {
            model.addAttribute("error", "Database error occurred while registering. Please try again.");
            return "index";
        }

        OtpVerification otp = verificationService.generateOtp(email);
        try {
            verificationService.sendOtpEmail(email, otp.getOtpCode());
            model.addAttribute("info", "OTP sent to " + email + ". Please check your inbox.");
            model.addAttribute("openOtpModal", true);
        } catch (org.springframework.mail.MailException ex) {
            model.addAttribute("error", "Could not send OTP email. Please verify SMTP credentials and try again.");
        }
        model.addAttribute("otpEmail", email);
        return "index";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String email, @RequestParam String otpCode, Model model) {
        boolean valid = verificationService.verifyOtp(email, otpCode);
        if (!valid) {
            model.addAttribute("error", "OTP invalid or expired");
            return "index";
        }
        model.addAttribute("success", "Email verified successfully");
        return "index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        Optional<User> user = userService.login(email, password);
        if (user.isEmpty()) {
            model.addAttribute("error", "Invalid credentials");
            return "index";
        }
        session.setAttribute("loggedUser", user.get());
        if ("ADMIN".equalsIgnoreCase(user.get().getRole())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null) {
            return "redirect:/";
        }
        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingService.listByCustomer(user.getId()));
        return "profile";
    }

    @PostMapping("/book")
    public String book(@RequestParam Long vehicleId,
                       @RequestParam String startDate,
                       @RequestParam String endDate,
                       HttpSession session,
                       Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !(user instanceof Customer)) {
            return "redirect:/";
        }
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        if (start.isBefore(LocalDate.now()) || end.isBefore(start)) {
            model.addAttribute("error", "Invalid booking dates");
            return "redirect:/";
        }
        if (bookingService.isConflict(vehicleId, start, end)) {
            model.addAttribute("error", "Vehicle already booked for selected date range");
            return "redirect:/";
        }
        Optional<Vehicle> vehicleOpt = vehicleService.getById(vehicleId);
        if (vehicleOpt.isEmpty()) {
            model.addAttribute("error", "Vehicle not found");
            return "redirect:/";
        }
        Booking booking = new Booking();
        booking.setCustomer((Customer) user);
        booking.setVehicle(vehicleOpt.get());
        booking.setStartDate(start);
        booking.setEndDate(end);
        bookingService.book(booking);
        model.addAttribute("success", "Booking placed successfully");
        return "redirect:/profile";
    }

    @PostMapping("/testimonial")
    public String testimonial(@RequestParam String message,
                              @RequestParam int rating,
                              HttpSession session,
                              Model model) {
        User user = (User) session.getAttribute("loggedUser");
        if (user == null || !(user instanceof Customer)) {
            return "redirect:/";
        }
        Testimonial t = new Testimonial();
        t.setCustomer((Customer) user);
        t.setMessage(message);
        t.setRating(rating);
        t.setStatus("PENDING");
        model.addAttribute("success", "Testimonial submitted for review");
        return "redirect:/profile";
    }
}
