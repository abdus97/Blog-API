package com.emumba.blogapi.controller;

import com.emumba.blogapi.client.dto.PostRequestDto;
import com.emumba.blogapi.dto.ErrorResponse;
import com.emumba.blogapi.dto.PostDto;
import com.emumba.blogapi.exception.ResourceNotFoundException;
import com.emumba.blogapi.model.Post;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.service.PostService;
import com.emumba.blogapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@Tag(name = "Post Management", description = "Endpoints for managing blog posts")
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;
    private final UserService userService;

    public PostController(PostService postService,UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('AUTHOR')")
    @Operation(summary = "Create a new blog post", description = "Create a new blog post with a title, content, optional tags, and an optional GitHub repository association.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })public ResponseEntity<?> createPost(@RequestBody PostRequestDto postRequestDto, Principal principal) {
        try {
            PostDto createdPost = postService.createPost(postRequestDto, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("(hasAuthority('AUTHOR') and @postService.isAuthor(#id, principal)) or hasAuthority('ADMIN')")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody Post updatedPost, Principal principal) {
        return ResponseEntity.ok(postService.updatePost(id, updatedPost));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("(hasAuthority('AUTHOR') and @postService.isAuthor(#id, principal)) or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deletePost(@PathVariable Long id, Principal principal) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<PostDto>> getPosts(@RequestParam(required = false, defaultValue = "true") boolean onlyPublished) {
        return ResponseEntity.ok(postService.getPosts(onlyPublished));
    }




}
