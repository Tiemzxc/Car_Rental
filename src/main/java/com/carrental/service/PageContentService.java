package com.carrental.service;

import com.carrental.model.PageContent;
import java.util.List;
import java.util.Optional;

public interface PageContentService {
    List<PageContent> listAll();
    Optional<PageContent> findByKey(String key);
    void saveOrUpdate(PageContent content);
}
