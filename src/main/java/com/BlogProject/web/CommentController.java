package com.BlogProject.web;

import com.BlogProject.po.Comment;
import com.BlogProject.po.User;
import com.BlogProject.service.BlogService;
import com.BlogProject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments", commentService.listCommentByBlogId(blogId));
        return "blog :: commentList";
    }

    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        User user = (User) session.getAttribute("user");
        //if user is not login, set the viewer's avatar,
        if(user == null){
            comment.setAvatar("/images/viewer-avatar.png");
        } else { //else set admin avatar
            comment.setAvatar("/images/admin-avatar.png");
            comment.setAdmin(true);
            comment.setNickname(user.getNickname());
        }
        commentService.saveComment(comment);
        return "redirect:/comments/" + blogId;
    }
}
