package com.carrental.service;

import com.carrental.model.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandService {
    List<Brand> listAll();
    Optional<Brand> getById(Long id);
    Brand save(Brand brand);
    void update(Brand brand);
    void delete(Long id);
}
