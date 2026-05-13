package com.carrental.service.impl;

import com.carrental.model.Brand;
import com.carrental.repository.BrandRepository;
import com.carrental.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> listAll() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getById(Long id) {
        return brandRepository.findById(id);
    }

    @Override
    public Brand save(Brand brand) {
        Long id = brandRepository.save(brand);
        brand.setId(id);
        return brand;
    }

    @Override
    public void update(Brand brand) {
        brandRepository.update(brand);
    }

    @Override
    public void delete(Long id) {
        brandRepository.deleteById(id);
    }
}
