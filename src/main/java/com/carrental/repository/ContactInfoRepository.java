package com.carrental.repository;

import com.carrental.model.ContactInfo;
import java.util.Optional;

public interface ContactInfoRepository {
    Optional<ContactInfo> findById(Long id);
    void save(ContactInfo info);
    void update(ContactInfo info);
}
