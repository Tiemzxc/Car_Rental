package com.carrental.service;

import com.carrental.model.ContactInfo;
import java.util.Optional;

public interface ContactInfoService {
    Optional<ContactInfo> findById(Long id);
    void save(ContactInfo info);
    void update(ContactInfo info);
}
