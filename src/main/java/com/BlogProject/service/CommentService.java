package com.BlogProject.service;

import com.BlogProject.po.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long id);

    Comment saveComment(Comment comment);

}
