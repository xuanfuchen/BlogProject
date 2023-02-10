package com.BlogProject.web;

import com.BlogProject.service.BlogService;
import com.BlogProject.service.TagService;
import com.BlogProject.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class indexController {

    private final Integer TOP_TYPE_LIST_SIZE = 6;
    private final Integer TOP_TAG_LIST_SIZE = 10;
    private final Integer RECOMMEND_BLOG_LIST_SIZE = 10;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                        Model model){
        model.addAttribute("page", blogService.publishedBlogs(pageable));
        model.addAttribute("types", typeService.topTypeList(TOP_TYPE_LIST_SIZE));
        model.addAttribute("tags", tagService.topTagList(TOP_TAG_LIST_SIZE));
        model.addAttribute("recommendBlogs", blogService.recommendBlogs(RECOMMEND_BLOG_LIST_SIZE));
        return "/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam String query,
                         @PageableDefault(size = 50, sort = {"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                         Model model){
        model.addAttribute("page", blogService.listBlog("%"+query+"%", pageable));
        model.addAttribute("searchInfo", query);
        return "/search";
    }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
        model.addAttribute("blog", blogService.getBlogHTML(id));
        return "/blog";
    }

    @GetMapping("/about-me")
    public String aboutMe(){
        return "/about-me";
    }
}
