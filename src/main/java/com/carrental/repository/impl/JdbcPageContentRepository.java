package com.carrental.repository.impl;

import com.carrental.model.PageContent;
import com.carrental.repository.PageContentRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPageContentRepository implements PageContentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPageContentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<PageContent> findAll() {
        String sql = "SELECT id,page_key,content FROM page_content";
        return jdbcTemplate.query(sql, new PageContentRowMapper());
    }

    @Override
    public Optional<PageContent> findByKey(String key) {
        String sql = "SELECT id,page_key,content FROM page_content WHERE page_key = ?";
        List<PageContent> list = jdbcTemplate.query(sql, new Object[]{key}, new PageContentRowMapper());
        return list.stream().findFirst();
    }

    @Override
    public void save(PageContent content) {
        String sql = "INSERT INTO page_content (page_key,content) VALUES (?,?)";
        jdbcTemplate.update(sql, content.getPageKey(), content.getContent());
    }

    @Override
    public void update(PageContent content) {
        String sql = "UPDATE page_content SET content=? WHERE page_key=?";
        jdbcTemplate.update(sql, content.getContent(), content.getPageKey());
    }

    private static class PageContentRowMapper implements RowMapper<PageContent> {
        @Override
        public PageContent mapRow(ResultSet rs, int rowNum) throws SQLException {
            PageContent c = new PageContent();
            c.setId(rs.getLong("id"));
            c.setPageKey(rs.getString("page_key"));
            c.setContent(rs.getString("content"));
            return c;
        }
    }
}
