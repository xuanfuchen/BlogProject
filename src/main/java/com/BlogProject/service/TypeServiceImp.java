package com.BlogProject.service;

import com.BlogProject.dao.TypeRepository;
import com.BlogProject.exception.NotFoundException;
import com.BlogProject.po.Blog;
import com.BlogProject.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TypeServiceImp implements TypeService{

    @Autowired
    private TypeRepository typeRepository;

    @Autowired
    private BlogService blogService;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.getReferenceById(id);
    }

    @Override
    public Type getType(Type type) {
        return typeRepository.getReferenceById(type.getId());
    }

    @Override
    public Type getTypeByName(String name){
        return typeRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getReferenceById(id);
        if (t == null){
            throw new NotFoundException("Type does not exist");
        }
        BeanUtils.copyProperties(type, t);
        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }

    @Override
    public List<Type> listAllType() {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        List<Type> listType = new ArrayList<>();
        for (Type type : typeRepository.findSortedType(sort)) {
            List<Blog> tempBlogs = new ArrayList<>();
            for(Blog blog : type.getBlogs()) {
                if(blog.isPublished()){
                    tempBlogs.add(blog);
                }
            }
            type.setBlogs(tempBlogs);
            listType.add(type);
        }
        return findPublishedBlogs(listType);
    }

    @Override
    public List<Type> topTypeList(Integer size){
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return findPublishedBlogs(typeRepository.findTop(pageable));
    }

    /**
     * delete all unpublished blogs in type.blog for each type in types
     * @param types
     * @return types with no draft blog in its blogs list
     */
    private List<Type> findPublishedBlogs(List<Type> types){
        List<Type> listType = new ArrayList<>();
        for (Type type : types) {
            List<Blog> tempBlogs = new ArrayList<>();
            for(Blog blog : type.getBlogs()) {
                if(blog.isPublished()){
                    tempBlogs.add(blog);
                }
            }
            type.setBlogs(tempBlogs);
            listType.add(type);
        }
        return listType;
    }
}
