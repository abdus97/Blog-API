package com.emumba.blogapi.service;


import com.emumba.blogapi.dto.CommentDto;
import com.emumba.blogapi.exception.ResourceNotFoundException;
import com.emumba.blogapi.mapper.Mapper;
import com.emumba.blogapi.model.Comment;
import com.emumba.blogapi.model.Post;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.repository.CommentRepository;
import com.emumba.blogapi.repository.PostRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
    }

    public CommentDto addComment(Long postId, String content, Principal principal) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        User user = userService.getUserByEmail(principal.getName());

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(sanitizeContent(content));
        commentRepository.save(comment);
        return Mapper.toCommentDto(comment);
    }

    public List<CommentDto> getCommentsForPost(Long postId) {
        // Fetch the post and handle the case where it doesn't exist
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        // Retrieve comments for the given post
        List<Comment> comments = commentRepository.findByPost(post);

        // Map each Comment entity to CommentDto
        return comments.stream()
                .map(Mapper::toCommentDto)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
        commentRepository.delete(comment);
    }

    public CommentDto replyToComment(Long commentId, String content, Principal principal) {
        Comment parentComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        User user = userService.getUserByEmail(principal.getName());

        Comment reply = new Comment();
        reply.setPost(parentComment.getPost());
        reply.setUser(user);
        reply.setContent(sanitizeContent(content));
        reply.setParent(parentComment);
        commentRepository.save(reply);
        return Mapper.toCommentDto(reply);
    }

    public CommentDto updateComment(Long commentId, String content, Principal principal) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        comment.setContent(sanitizeContent(content));
        commentRepository.save(comment);
        return  Mapper.toCommentDto(comment);
    }

    public boolean isAuthor(Long commentId, UserDetails userDetails) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        return comment != null && comment.getUser().getEmail().equals(userDetails.getUsername());
    }

    private String sanitizeContent(String content) {
        return content.trim().replaceAll("\\s+", " ").replaceAll("^\"|\"$", "");
    }
}
