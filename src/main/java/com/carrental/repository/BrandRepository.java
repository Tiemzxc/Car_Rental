package com.carrental.repository;

import com.carrental.model.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandRepository {
    Long save(Brand brand);
    Optional<Brand> findById(Long id);
    List<Brand> findAll();
    void update(Brand brand);
    void deleteById(Long id);
}
