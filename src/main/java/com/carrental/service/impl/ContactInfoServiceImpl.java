package com.carrental.service.impl;

import com.carrental.model.ContactInfo;
import com.carrental.repository.ContactInfoRepository;
import com.carrental.service.ContactInfoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ContactInfoServiceImpl implements ContactInfoService {
    private final ContactInfoRepository repository;

    public ContactInfoServiceImpl(ContactInfoRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<ContactInfo> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public void save(ContactInfo info) {
        repository.save(info);
    }

    @Override
    public void update(ContactInfo info) {
        repository.update(info);
    }
}
