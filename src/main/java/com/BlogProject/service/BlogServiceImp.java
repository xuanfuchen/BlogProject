package com.BlogProject.service;

import com.BlogProject.dao.BlogRepository;
import com.BlogProject.exception.NotFoundException;
import com.BlogProject.po.Blog;
import com.BlogProject.po.Type;
import com.BlogProject.util.MarkdownUtils;
import com.BlogProject.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

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
     * find blog by ID and convert the content from Markdown to HTML
     * @param id
     * @return Blog with HTML content
     */

    @Transactional
    @Override
    public Blog getBlogHTML(Long id) {
        Blog blog = blogRepository.getReferenceById(id);
        //view++ each time the blog is refreshed
        blogRepository.updateView(id);
        if(blog == null){
            throw new NotFoundException("Blog does not exist");
        }
        Blog blogCopy = new Blog();
        //make a deep copy to the blog since JPA might auto save the changed content to the database
        BeanUtils.copyProperties(blog, blogCopy);
        String content = blogCopy.getContent();
        content = MarkdownUtils.markdownToHtmlExtensions(content);
        blogCopy.setContent(content);
        return blogCopy;
    }

    /**
     * for the search feature in blog admin page
     * @param pageable
     * @param blog
     * @return A page of blogs that meets the required name/type/recommend/draft status
     */
    @Override
    public Page<Blog> listBlog(Pageable pageable, Blog blog) {
        return blogRepository.findAll(new Specification<Blog>() { //don't really know how it works
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
                if(!blog.isPublished()){
                    predicates.add(cb.equal(root.<Boolean>get("published"), blog.isPublished()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    /**
     * This method can find Published blogs that meets the required name/type/recommend
     * @param pageable
     * @param blog
     * @return A page of blogs that meets the required name/type/recommend status
     */
    @Override
    public Page<Blog> pagePublishedBlogs(Pageable pageable, Blog blog) {
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
                if(blog.isPublished()){
                    predicates.add(cb.equal(root.<Boolean>get("published"), blog.isPublished()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    /**
     * Find all blogs that have tag with tagId
     * @param pageable
     * @param tagId
     * @return a page object that contains blogs with blog.tag.id = tagId
     */
    @Override
    public Page<Blog> pagePublishedBlogsByTagId(Pageable pageable, Long tagId) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //join blog and tags table
                Join join = root.join("tags");
                //query where join.id = tagId
                predicates.add(cb.equal(join.get("id"), tagId));
                //query where join.published = true
                predicates.add(cb.equal(root.<Boolean>get("published"), true));
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
        return blogRepository.findPublishedByQuery(query, pageable);
    }

    /**
     *
     * @return a map contains years as key and value as blogs posted in that year <K, V> -> <year, List<Blog>>
     */
    @Override
    public Map<Integer, List<Blog>> archiveBlogs() {
        List<Integer> years = blogRepository.findDistinctYears();
        //Use LinkedHashMap because we want ordered key
        Map<Integer, List<Blog>> map = new LinkedHashMap<>();
        for(Integer year : years){
            List<Blog> listBlog = new ArrayList<>();
            listBlog = blogRepository.findBlogsByYear(year);
            map.put(year, listBlog);
        }
        return map;
    }

    /**
     * @return count of all published blogs
     */

    @Override
    public Integer countAllPublishedBlogs() {
        return blogRepository.countPublishedBlogs();
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
