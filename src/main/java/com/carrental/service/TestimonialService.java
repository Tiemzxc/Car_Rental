package com.carrental.service;

import com.carrental.model.Testimonial;
import java.util.List;
import java.util.Optional;

public interface TestimonialService {
    Testimonial submit(Testimonial t);
    List<Testimonial> listAll();
    Optional<Testimonial> findById(Long id);
    void approve(Long id);
    void reject(Long id);
    void delete(Long id);
}
