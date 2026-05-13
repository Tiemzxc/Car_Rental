package com.carrental.service.impl;

import com.carrental.model.Testimonial;
import com.carrental.repository.TestimonialRepository;
import com.carrental.service.TestimonialService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TestimonialServiceImpl implements TestimonialService {
    private final TestimonialRepository repository;

    public TestimonialServiceImpl(TestimonialRepository repository) {
        this.repository = repository;
    }

    @Override
    public Testimonial submit(Testimonial t) {
        Long id = repository.save(t);
        t.setId(id);
        return t;
    }

    @Override
    public List<Testimonial> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<Testimonial> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void approve(Long id) {
        repository.updateStatus(id, "APPROVED");
    }

    @Override
    public void reject(Long id) {
        repository.updateStatus(id, "REJECTED");
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
