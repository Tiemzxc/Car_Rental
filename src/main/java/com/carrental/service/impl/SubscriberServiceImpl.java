package com.carrental.service.impl;

import com.carrental.model.Subscriber;
import com.carrental.repository.SubscriberRepository;
import com.carrental.service.SubscriberService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberServiceImpl implements SubscriberService {
    private final SubscriberRepository repository;

    public SubscriberServiceImpl(SubscriberRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Subscriber> listAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
