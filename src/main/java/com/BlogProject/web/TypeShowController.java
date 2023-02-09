package com.BlogProject.web;

import com.BlogProject.po.Blog;
import com.BlogProject.po.Type;
import com.BlogProject.service.BlogService;
import com.BlogProject.service.TypeService;
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
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String type(@PathVariable Long id,
                       @PageableDefault(size=10, sort={"updateTime"}, direction = Sort.Direction.DESC)Pageable pageable,
                       Model model){
        List<Type> types = typeService.listAllType();
        //id == -1 means user are navigate from other pages to types page
        //then use the id of first type on the list as default id
        if(id == -1){
            id = types.get(0).getId();
        }
        Blog blogQuery = new Blog();
        Type tempType = new Type();
        tempType.setId(id);
        blogQuery.setType(tempType);
        blogQuery.setPublished(true);
        model.addAttribute("types", types);
        model.addAttribute("page", blogService.pagePublishedBlogs(pageable, blogQuery));
        model.addAttribute("activeTypeId", id);
        return "types";
    }
}
