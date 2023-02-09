package com.BlogProject.web;

import com.BlogProject.po.Blog;
import com.BlogProject.po.Tag;
import com.BlogProject.po.Type;
import com.BlogProject.service.BlogService;
import com.BlogProject.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;

    @Autowired
    private BlogService blogService;

    @GetMapping("tags/{id}")
    public String types(@PathVariable Long id,
                        @PageableDefault(size=10, sort={"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<Tag> tags = tagService.listAllTag();
        //id == -1 means user are navigate from other pages to types page
        //then use the id of first type on the list as default id
        if(id == -1){
            id = tags.get(0).getId();
        }
        model.addAttribute("tags", tags);
        model.addAttribute("page", blogService.pagePublishedBlogsByTagId(pageable, id));
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
