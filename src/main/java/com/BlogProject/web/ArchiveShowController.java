package com.BlogProject.web;

import com.BlogProject.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/archive")
    public String archive(Model model){
        model.addAttribute("archiveMap", blogService.archiveBlogs());
        model.addAttribute("blogsCount", blogService.countAllPublishedBlogs());
        return "archive";
    }
}
