package com.emumba.blogapi.controller;

import com.emumba.blogapi.dto.CommentDto;
import com.emumba.blogapi.model.Comment;
import com.emumba.blogapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comments")
    @PreAuthorize("hasAuthority('READER')")
    public ResponseEntity<CommentDto> addComment(@PathVariable Long postId, @RequestBody String content, Principal principal) {
        CommentDto comment = commentService.addComment(postId, content, principal);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable Long postId) {
        List<CommentDto> comments = commentService.getCommentsForPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/comments/{commentId}/reply")
    @PreAuthorize("hasAuthority('READER')")
    public ResponseEntity<CommentDto> replyToComment(@PathVariable Long commentId, @RequestBody String content, Principal principal) {
        CommentDto reply = commentService.replyToComment(commentId, content, principal);
        return new ResponseEntity<>(reply, HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("hasAuthority('ADMIN') or @commentService.isAuthor(#commentId, principal)")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/comments/{commentId}")
    @PreAuthorize("hasAuthority('READER') and @commentService.isAuthor(#commentId, principal)")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long commentId, @RequestBody String content, Principal principal) {
        CommentDto updatedComment = commentService.updateComment(commentId, content, principal);
        return ResponseEntity.ok(updatedComment);
    }
}

