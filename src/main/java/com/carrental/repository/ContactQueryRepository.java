package com.carrental.repository;

import com.carrental.model.ContactQuery;
import java.util.List;

public interface ContactQueryRepository {
    List<ContactQuery> findAll();
    void deleteById(Long id);
}
