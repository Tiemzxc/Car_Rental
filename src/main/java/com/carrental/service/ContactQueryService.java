package com.carrental.service;

import com.carrental.model.ContactQuery;
import java.util.List;

public interface ContactQueryService {
    List<ContactQuery> listAll();
    void delete(Long id);
}
