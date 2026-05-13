package com.carrental.service.impl;

import com.carrental.model.ContactQuery;
import com.carrental.repository.ContactQueryRepository;
import com.carrental.service.ContactQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactQueryServiceImpl implements ContactQueryService {
    private final ContactQueryRepository repository;

    public ContactQueryServiceImpl(ContactQueryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ContactQuery> listAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
