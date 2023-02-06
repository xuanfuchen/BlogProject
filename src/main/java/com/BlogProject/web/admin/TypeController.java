package com.BlogProject.web.admin;

import com.BlogProject.po.Type;
import com.BlogProject.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @GetMapping("/typeManage")
    public String typeManage(@PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        model.addAttribute("page", typeService.listType(pageable));
        return "/admin/typeManage";
    }

    @GetMapping("/typeManage/addType")
    public String add(Model model){
        model.addAttribute("type", new Type());
        return "/admin/typeInput";
    }

    //use POST method to save types and avoid the collision under URL "/typeManage"
    @PostMapping("/typeManage")
    public String addType(@Valid Type type, BindingResult result, RedirectAttributes attributes) {
        //check if there is a duplicate type name in the database already, if so, add an error into the validation check result
        if(typeService.getTypeByName(type.getName()) != null){
            result.rejectValue("name", "nameError", "Cannot have duplicate type name");
        }
        if (result.hasErrors()){
            return "/admin/typeInput";
        }
        Type t = typeService.saveType(type);
        if (t == null) {
            attributes.addFlashAttribute("message", "Oops, something has gone wrong. The type was not added.");
        }else{
            attributes.addFlashAttribute("message", "The type has been successfully ADDED.");
        }
        return "redirect:/admin/typeManage";
    }

    @GetMapping("/typeManage/{id}/edit")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("type", typeService.getType(id));
        return "admin/typeInput";
    }

    @PostMapping("/typeManage/{id}")
    public String editType(Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
        if(typeService.getTypeByName(type.getName()) != null){
            result.rejectValue("name", "nameError", "Cannot have duplicate type name");
        }
        if (result.hasErrors()){
            return "/admin/typeInput";
        }
        Type t = typeService.updateType(id, type);
        if (t == null) {
            attributes.addFlashAttribute("message", "Oops, something has gone wrong. The type was not updated.");
        }else{
            attributes.addFlashAttribute("message", "The type has been successfully UPDATED.");
        }
        return "redirect:/admin/typeManage";
    }

    @GetMapping("/typeManage/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message", "The type has been successfully DELETED.");
        return "redirect:/admin/typeManage";
    }
}
