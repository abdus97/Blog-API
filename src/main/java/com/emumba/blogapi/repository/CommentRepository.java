package com.emumba.blogapi.repository;

import com.emumba.blogapi.model.Comment;
import com.emumba.blogapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // Find all comments for a specific post
    List<Comment> findByPost(Post post);
}
