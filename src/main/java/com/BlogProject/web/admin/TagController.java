package com.BlogProject.web.admin;

import com.BlogProject.po.Tag;
import com.BlogProject.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.jar.Attributes;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tagManage")
    public String tagManage(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                             Model model){
        model.addAttribute("page", tagService.listTag(pageable));
        return "/admin/tagManage";
    }

    @GetMapping("/tagManage/addTag")
    public String add(Model model){
        model.addAttribute("tag", new Tag());
        return "/admin/tagInput";
    }

    @PostMapping("/tagManage")
    public String addTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        if(tagService.getTagByName(tag.getName()) != null){
            result.rejectValue("name", "nameError", "Cannot have duplicate tag name");
        }
        if (result.hasErrors()){
            return "/admin/tagInput";
        }
        Tag t = tagService.saveTag(tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "Oops, something has gone wrong. The type was not added.");
        }else{
            attributes.addFlashAttribute("message", "The type has been successfully ADDED.");
        }
        return "redirect:/admin/tagManage";
    }

    @GetMapping("/tagManage/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("tag", tagService.getTagById(id));
        return "/admin/tagInput";
    }

    @PostMapping("/tagManage/{id}")
    public String editTag(@PathVariable Long id, @Valid Tag tag, BindingResult result, RedirectAttributes attributes){
        if(tagService.getTagByName(tag.getName()) != null){
            result.rejectValue("name", "nameError", "Cannot have duplicate tag name");
        }
        if (result.hasErrors()){
            return "/admin/tagInput";
        }
        Tag t = tagService.updateTag(id, tag);
        if (t == null) {
            attributes.addFlashAttribute("message", "Oops, something has gone wrong. The tag was NOT UPDATED.");
        }else{
            attributes.addFlashAttribute("message", "The tag has been successfully UPDATED.");
        }
        return "redirect:/admin/tagManage";
    }

    @GetMapping("/tagManage/{id}/delete")
    public String deleteTag(@PathVariable Long id, RedirectAttributes attributes){
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "The tag has been successfully DELETED.");
        return "redirect:/admin/tagManage";
    }
}
