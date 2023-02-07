package com.BlogProject.service;

import com.BlogProject.dao.BlogRepository;
import com.BlogProject.exception.NotFoundException;
import com.BlogProject.po.Blog;
import com.BlogProject.po.Type;
import com.BlogProject.util.MyBeanUtils;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BlogServiceImp implements BlogService{

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getReferenceById(id);
    }

    @Override
    public Blog getBlog(String name) {
        return null;
    }

    /**
     * This method serves the searching feature in
     * @param pageable
     * @param blog
     * @return A page of blogs that meets the required name/type/recommend status
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, Blog blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(blog.getTitle() != null && !blog.getTitle().equals("")){
                    predicates.add(cb.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if(blog.getType() != null && blog.getType().getId() != null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"), blog.getType().getId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    /**
     * This method return all blogs as a Page object
     * @param pageable
     * @return Page<Blog> that contains all blogs
     */

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        //if it's a new blog, initialize create time, update time, and view
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setView(0);

        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getReferenceById(id);
        if(b == null){
            throw new NotFoundException("Blog does not exist");
        }
        blog.setUpdateTime(new Date());
        BeanUtils.copyProperties(blog, b, MyBeanUtils.getNullPropertyNames(blog));
        return blogRepository.save(b);
    }

    @Override
    public Page<Blog> publishedBlogs(Pageable pageable) {
        return blogRepository.findPublished(pageable);
    }

    @Override
    public List<Blog> recommendBlogs(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0,size,sort);
        return blogRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }
}
