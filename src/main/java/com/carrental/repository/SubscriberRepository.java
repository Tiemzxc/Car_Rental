package com.carrental.repository;

import com.carrental.model.Subscriber;
import java.util.List;

public interface SubscriberRepository {
    List<Subscriber> findAll();
    void deleteById(Long id);
}
