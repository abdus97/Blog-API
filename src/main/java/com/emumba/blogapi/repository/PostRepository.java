package com.emumba.blogapi.repository;



import com.emumba.blogapi.model.Post;
import com.emumba.blogapi.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByAuthorEmail(String email); // Retrieve posts by author email
    List<Post> findByPublishedTrue();
    List<Post> findByPublishedFalse(); // Method to find drafts
    List<Post> findByAuthorAndPublishedFalse(User author);
}
