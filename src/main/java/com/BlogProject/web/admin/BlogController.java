package com.BlogProject.web.admin;

import com.BlogProject.po.Blog;
import com.BlogProject.service.BlogService;
import com.BlogProject.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @GetMapping("/blogManage")
    public String blogManage(Model model, Blog blog,
                             @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                             Pageable pageable){
        //we need all types and a page of blogs for one blogManage page
        model.addAttribute("types", typeService.listAllType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogManage";
    }

    @PostMapping("/blogManage/search")
    public String search(Model model, Blog blog,
                             @PageableDefault(size = 3, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                             Pageable pageable){
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogManage :: blogList";
    }
}
