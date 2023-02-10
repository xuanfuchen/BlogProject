package com.BlogProject.service;

import com.BlogProject.dao.CommentRepository;
import com.BlogProject.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImp implements CommentService{

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        //get all root comments that has no parent comment
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentIsNull(blogId, sort);
        //turn whole comment tree into height of 2, and return it
        List<Comment> result = twoLayerComments(comments);
        return result;
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        //default parentId = -1 if the comment has no parent comment
        Long parentId = comment.getParentComment().getId();
        //if there is parent comment, setParentComment
        if(parentId != -1){
            comment.setParentComment(commentRepository.getReferenceById(parentId));
        } else { //if there is no parent comment, set parentComment to null for database storage
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    /**
     * Find all root comments and combine all their descendant nodes into direct child comments
     * @param comments
     * @return root comments with comment trees that have height of 2
     */
    private List<Comment> twoLayerComments(List<Comment> comments) {
        List<Comment> rootComments = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            //deep copy to avoid affecting database
            BeanUtils.copyProperties(comment,c);
            rootComments.add(c);
        }
        //combine all descendant nodes, and make them become the direct children of the root comment
        //because we are making a comment list with only 2 layers
        combineChildren(rootComments);
        return rootComments;
    }

    //For temperately store all descendant nodes of a root node
    private List<Comment> tempDescendants = new ArrayList<>();
    /**
     * This method returns all
     * @param comments that are root comments (with no parent comment)
     * @return void
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replies = comment.getReplyComments();
            for(Comment reply : replies) {
                //iterate through all replies
                //and for each reply, recursively find all of its descendant and add it to tempDescendants
                addAllDescendant(reply);
            }
            //set the ReplyComments of root node as all of its descendant
            comment.setReplyComments(tempDescendants);
            //new a new tempDescendants for next iteration
            //Do not use .clear() since it's already assigned to comment.ReplyComments
            tempDescendants = new ArrayList<>();
        }
    }
    /**
     * Recursively add all descendants of input comment into tempDescendants
     * @param comment
     * @return void
     */
    private void addAllDescendant(Comment comment) {
        //add current comment to the tempReplies if it's not null
        tempDescendants.add(comment);

        //if there is descendant comments under current comment
        if (comment.getReplyComments().size()>0) {
            //get all replies under current comment
            List<Comment> replies = comment.getReplyComments();
            //iterate through all replies, recursively add all descendants into tempDescendants
            for (Comment reply : replies) {
                addAllDescendant(reply);
            }
        }
        //else end the recursion
    }
}
