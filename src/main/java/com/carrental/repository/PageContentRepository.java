package com.carrental.repository;

import com.carrental.model.PageContent;
import java.util.List;
import java.util.Optional;

public interface PageContentRepository {
    List<PageContent> findAll();
    Optional<PageContent> findByKey(String key);
    void save(PageContent content);
    void update(PageContent content);
}
