package com.carrental.service;

import com.carrental.model.Subscriber;
import java.util.List;

public interface SubscriberService {
    List<Subscriber> listAll();
    void delete(Long id);
}
