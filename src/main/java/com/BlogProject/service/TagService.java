package com.BlogProject.service;

import com.BlogProject.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTagById(Long id);

    Tag getTagByName(String name);

    Page<Tag> listTag(Pageable pageable);

    List<Tag> listAllTag();

    List<Tag> topTagList(Integer size);

    List<Tag> getTags(List<Tag> list);

    String getTagIds(List<Tag> listTag);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);
}
