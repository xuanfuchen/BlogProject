package com.BlogProject.web.admin;

import com.BlogProject.po.Blog;
import com.BlogProject.po.Tag;
import com.BlogProject.po.User;
import com.BlogProject.service.BlogService;
import com.BlogProject.service.TagService;
import com.BlogProject.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogManage")
    public String blogManage(Model model, Blog blog,
                             @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                             Pageable pageable){
        //we need all types and a page of blogs for one blogManage page
        model.addAttribute("types", typeService.listAllType());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "/admin/blogManage";
    }

    @PostMapping("/blogManage/search")
    public String search(Model model, Blog blog,
                             @PageableDefault(size = 10, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                             Pageable pageable){
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "/admin/blogManage :: blogList";
    }

    @GetMapping("blogManage/blogInput")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return "/admin/blogInput";
    }

    private void setTypeAndTag(Model model){
        model.addAttribute("types", typeService.listAllType());
        model.addAttribute("tags", tagService.listAllTag());
    }

    //set attributes to blog object,
    //if it's a new blog, save it into the database
    //otherwise update the existing blog
    @PostMapping("/blogManage")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType()));
        blog.setTags(tagService.getTags(blog.getTags()));
        if(blog.getId() == null){
            blogService.saveBlog(blog);
        } else {
            blogService.updateBlog(blog.getId(), blog);
        }

        return "redirect:/admin/blogManage";
    }

    @GetMapping("/blogManage/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "The tag has been successfully DELETED.");
        return "redirect:/admin/blogManage";
    }

    @GetMapping("blogManage/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("tagIds", tagService.getTagIds(blogService.getBlog(id).getTags()));
        setTypeAndTag(model);
        //form a string of tags' id for front end pull down menu's use
        model.addAttribute("blog", blogService.getBlog(id));
        return "/admin/blogInput";
    }
}
