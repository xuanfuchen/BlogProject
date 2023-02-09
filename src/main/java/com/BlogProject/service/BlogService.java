package com.BlogProject.service;

import com.BlogProject.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id);

    Blog getBlog(String name);

    Blog getBlogHTML(Long id);

    Page<Blog> listBlog(Pageable pageable, Blog blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(String query, Pageable pageable);

    Page<Blog> pagePublishedBlogs(Pageable pageable, Blog blog);

    Page<Blog> pagePublishedBlogsByTagId(Pageable pageable, Long tagId);

    List<Blog> recommendBlogs(Integer size);

    Page<Blog> publishedBlogs(Pageable pageable);

    Map<Integer, List<Blog>> archiveBlogs();

    Integer countAllPublishedBlogs();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);
}
