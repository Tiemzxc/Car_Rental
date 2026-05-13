package com.carrental.service.impl;

import com.carrental.model.PageContent;
import com.carrental.repository.PageContentRepository;
import com.carrental.service.PageContentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageContentServiceImpl implements PageContentService {
    private final PageContentRepository repository;

    public PageContentServiceImpl(PageContentRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PageContent> listAll() {
        return repository.findAll();
    }

    @Override
    public Optional<PageContent> findByKey(String key) {
        return repository.findByKey(key);
    }

    @Override
    public void saveOrUpdate(PageContent content) {
        Optional<PageContent> existing = repository.findByKey(content.getPageKey());
        if (existing.isPresent()) {
            content.setId(existing.get().getId());
            repository.update(content);
        } else {
            repository.save(content);
        }
    }
}
