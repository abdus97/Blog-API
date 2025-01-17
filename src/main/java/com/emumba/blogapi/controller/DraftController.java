package com.emumba.blogapi.controller;

import com.emumba.blogapi.client.dto.PostRequestDto;
import com.emumba.blogapi.dto.PostDto;
import com.emumba.blogapi.model.Post;
import com.emumba.blogapi.service.PostService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts/drafts")
public class DraftController {

    private final PostService postService;


    public DraftController(PostService postService) {
        this.postService = postService;

    }
    @PostMapping
    @PreAuthorize("hasAuthority('AUTHOR') or hasAuthority('ADMIN')")
    public ResponseEntity<PostDto> createDraft(@Valid @RequestBody PostRequestDto post, Principal principal) {
        return new ResponseEntity<>(postService.saveDraft(post, principal), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('AUTHOR') or hasAuthority('ADMIN')")
    public ResponseEntity<List<PostDto>> getDrafts(Principal principal) {
        return ResponseEntity.ok(postService.getDraftsByAuthor(principal));
    }

    @PutMapping("/{id}")
    @PreAuthorize("(hasAuthority('AUTHOR') and @postService.isAuthor(#id, principal)) or hasAuthority('ADMIN')")
    public ResponseEntity<PostDto> updateDraft(@PathVariable Long id, @Valid @RequestBody Post post, Principal principal) {
        return ResponseEntity.ok(postService.updateDraft(id, post));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("(hasAuthority('AUTHOR') and @postService.isAuthor(#id, principal)) or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteDraft(@PathVariable Long id, Principal principal) {
        postService.deleteDraft(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("(@postService.isAuthor(#id, principal) and hasAuthority('AUTHOR')) or hasAuthority('ADMIN')")
    public ResponseEntity<PostDto> publishDraft(@PathVariable Long id, Principal principal) {
        return ResponseEntity.ok(postService.publishDraft(id, principal));
    }



}
