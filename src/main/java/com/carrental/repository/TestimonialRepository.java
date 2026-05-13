package com.carrental.repository;

import com.carrental.model.Testimonial;
import java.util.List;
import java.util.Optional;

public interface TestimonialRepository {
    Long save(Testimonial t);
    List<Testimonial> findAll();
    Optional<Testimonial> findById(Long id);
    void updateStatus(Long id, String status);
    void deleteById(Long id);
}
