package com.BlogProject.service;

import com.BlogProject.dao.TagRepository;
import com.BlogProject.exception.NotFoundException;
import com.BlogProject.po.Blog;
import com.BlogProject.po.Tag;
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
public class TagServiceImp implements TagService{

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTagById(Long id) {
        return tagRepository.getReferenceById(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listAllTag() {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        return removeDraftBlogs(tagRepository.findSortedTags(sort));
    }

    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getReferenceById(id);
        if(t == null){
            throw new NotFoundException("Tag does not exist");
        }
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(t);
    }

    @Override
    public List<Tag> getTags(List<Tag> list) {
        List<Long> idList = new ArrayList<>();
        for(Tag tag : list){
            idList.add(tag.getId());
        }
        return tagRepository.findAllById(idList);
    }

    @Override
    public String getTagIds(List<Tag> Tags) {
        StringBuilder sb = new StringBuilder();
        for(Tag tag: Tags){
            sb.append(tag.getId());
            sb.append(",");
        }
        String tagIds = sb.toString();
        if (tagIds.length() > 1) {
            tagIds = tagIds.substring(0, tagIds.length() - 1);
        }
        return tagIds;
    }

    @Override
    public List<Tag> topTagList(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return removeDraftBlogs(tagRepository.findTop(pageable));
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    /**
     * delete all unpublished blogs in type.blog for each type in types
     * @param tags
     * @return types with no draft blog in its blogs list
     */
    private List<Tag> removeDraftBlogs(List<Tag> tags){
        List<Tag> listType = new ArrayList<>();
        for (Tag tag : tags) {
            List<Blog> tempBlogs = new ArrayList<>();
            for(Blog blog : tag.getBlogs()) {
                if(blog.isPublished()){
                    tempBlogs.add(blog);
                }
            }
            tag.setBlogs(tempBlogs);
            listType.add(tag);
        }
        return listType;
    }
}
