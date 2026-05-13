package com.carrental.controller;

import com.carrental.model.*;
import com.carrental.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final BrandService brandService;
    private final VehicleService vehicleService;
    private final BookingService bookingService;
    private final TestimonialService testimonialService;
    private final ContactQueryService contactQueryService;
    private final SubscriberService subscriberService;
    private final UserService userService;
    private final PageContentService pageContentService;
    private final ContactInfoService contactInfoService;

    public AdminController(BrandService brandService, VehicleService vehicleService, BookingService bookingService, TestimonialService testimonialService,
                           ContactQueryService contactQueryService, SubscriberService subscriberService, UserService userService,
                           PageContentService pageContentService, ContactInfoService contactInfoService) {
        this.brandService = brandService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
        this.testimonialService = testimonialService;
        this.contactQueryService = contactQueryService;
        this.subscriberService = subscriberService;
        this.userService = userService;
        this.pageContentService = pageContentService;
        this.contactInfoService = contactInfoService;
    }

    private boolean isAdmin(HttpSession session) {
        Object user = session.getAttribute("loggedUser");
        if (user instanceof User) {
            return "ADMIN".equalsIgnoreCase(((User) user).getRole());
        }
        return false;
    }

    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("brandCount", brandService.listAll().size());
        model.addAttribute("vehicleCount", vehicleService.listAll().size());
        model.addAttribute("bookingCount", bookingService.listAll().size());
        model.addAttribute("testimonialCount", testimonialService.listAll().size());
        model.addAttribute("contactCount", contactQueryService.listAll().size());
        model.addAttribute("subscriberCount", subscriberService.listAll().size());
        model.addAttribute("userCount", userService.getAllUsers().size());
        return "admin/dashboard";
    }

    @GetMapping("/admin/brands")
    public String brands(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("brands", brandService.listAll());
        return "admin/brands";
    }

    @PostMapping("/admin/brands/create")
    public String createBrand(HttpSession session, @RequestParam String name) {
        if (!isAdmin(session)) return "redirect:/";
        Brand b = new Brand();
        b.setName(name);
        brandService.save(b);
        return "redirect:/admin/brands";
    }

    @PostMapping("/admin/brands/update")
    public String updateBrand(HttpSession session, @RequestParam Long id, @RequestParam String name) {
        if (!isAdmin(session)) return "redirect:/";
        Brand b = new Brand();
        b.setId(id);
        b.setName(name);
        brandService.update(b);
        return "redirect:/admin/brands";
    }

    @PostMapping("/admin/brands/delete")
    public String deleteBrand(HttpSession session, @RequestParam Long id) {
        if (!isAdmin(session)) return "redirect:/";
        brandService.delete(id);
        return "redirect:/admin/brands";
    }

    @GetMapping("/admin/vehicles")
    public String vehicles(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("vehicles", vehicleService.listAll());
        model.addAttribute("brands", brandService.listAll());
        return "admin/vehicles";
    }

    @PostMapping("/admin/vehicles/save")
    public String saveVehicle(HttpSession session,
                              @RequestParam String modelName,
                              @RequestParam String type,
                              @RequestParam Long brandId,
                              @RequestParam String imageUrl,
                              @RequestParam String pricePerDay,
                              @RequestParam(defaultValue = "true") boolean available) {
        if (!isAdmin(session)) return "redirect:/";
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(modelName);
        vehicle.setType(type);
        Brand brand = brandService.getById(brandId).orElse(null);
        vehicle.setBrand(brand);
        vehicle.setImageUrl(imageUrl);
        vehicle.setPricePerDay(new java.math.BigDecimal(pricePerDay));
        vehicle.setAvailable(available);
        vehicleService.save(vehicle);
        return "redirect:/admin/vehicles";
    }

    @PostMapping("/admin/vehicles/delete")
    public String deleteVehicle(HttpSession session, @RequestParam Long id) {
        if (!isAdmin(session)) return "redirect:/";
        vehicleService.delete(id);
        return "redirect:/admin/vehicles";
    }

    @GetMapping("/admin/bookings")
    public String adminBookings(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("bookings", bookingService.listAll());
        return "admin/bookings";
    }

    @PostMapping("/admin/bookings/update")
    public String updateBooking(HttpSession session, @RequestParam Long id, @RequestParam String status) {
        if (!isAdmin(session)) return "redirect:/";
        bookingService.findById(id).ifPresent(b -> {
            b.setStatus(status);
            bookingService.book(b);
        });
        return "redirect:/admin/bookings";
    }

    @GetMapping("/admin/testimonials")
    public String adminTestimonials(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("testimonials", testimonialService.listAll());
        return "admin/testimonials";
    }

    @PostMapping("/admin/testimonials/approve")
    public String approveTestimonial(HttpSession session, @RequestParam Long id) {
        if (!isAdmin(session)) return "redirect:/";
        testimonialService.approve(id);
        return "redirect:/admin/testimonials";
    }

    @PostMapping("/admin/testimonials/reject")
    public String rejectTestimonial(HttpSession session, @RequestParam Long id) {
        if (!isAdmin(session)) return "redirect:/";
        testimonialService.reject(id);
        return "redirect:/admin/testimonials";
    }

    @GetMapping("/admin/contact-queries")
    public String adminContactQueries(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("queries", contactQueryService.listAll());
        return "admin/contact-queries";
    }

    @PostMapping("/admin/contact-queries/delete")
    public String deleteQuery(HttpSession session, @RequestParam Long id) {
        if (!isAdmin(session)) return "redirect:/";
        contactQueryService.delete(id);
        return "redirect:/admin/contact-queries";
    }

    @GetMapping("/admin/users")
    public String adminUsers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/admin/subscribers")
    public String adminSubscribers(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("subscribers", subscriberService.listAll());
        return "admin/subscribers";
    }

    @PostMapping("/admin/subscribers/delete")
    public String deleteSubscriber(HttpSession session, @RequestParam Long id) {
        if (!isAdmin(session)) return "redirect:/";
        subscriberService.delete(id);
        return "redirect:/admin/subscribers";
    }

    @GetMapping("/admin/page-content")
    public String pageContent(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        model.addAttribute("pageContents", pageContentService.listAll());
        return "admin/page-content";
    }

    @PostMapping("/admin/page-content/update")
    public String updatePageContent(HttpSession session, @RequestParam String pageKey, @RequestParam String content) {
        if (!isAdmin(session)) return "redirect:/";
        PageContent pageContent = new PageContent();
        pageContent.setPageKey(pageKey);
        pageContent.setContent(content);
        pageContentService.saveOrUpdate(pageContent);
        return "redirect:/admin/page-content";
    }

    @GetMapping("/admin/contact-info")
    public String contactInfo(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/";
        ContactInfo info = contactInfoService.findById(1L).orElse(new ContactInfo());
        model.addAttribute("contactInfo", info);
        return "admin/contact-info";
    }

    @PostMapping("/admin/contact-info/update")
    public String updateContactInfo(HttpSession session, @RequestParam String phone, @RequestParam String email, @RequestParam String address) {
        if (!isAdmin(session)) return "redirect:/";
        ContactInfo info = contactInfoService.findById(1L).orElse(new ContactInfo());
        info.setPhone(phone);
        info.setEmail(email);
        info.setAddress(address);
        if (info.getId() == null) {
            contactInfoService.save(info);
        } else {
            contactInfoService.update(info);
        }
        return "redirect:/admin/contact-info";
    }
}

