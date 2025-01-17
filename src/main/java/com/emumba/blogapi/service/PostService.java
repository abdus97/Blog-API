package com.emumba.blogapi.service;

import com.emumba.blogapi.client.dto.PostRequestDto;
import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubRepo;
import com.emumba.blogapi.client.repository.GitHubIssueRepository;
import com.emumba.blogapi.client.repository.GitHubRepoRepository;
import com.emumba.blogapi.dto.PostDto;
import com.emumba.blogapi.exception.ResourceNotFoundException;
import com.emumba.blogapi.mapper.Mapper;
import com.emumba.blogapi.model.Post;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.repository.PostRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;




@Service
public class PostService {
    private final PostRepository postRepository;
    private  final UserService userService;
    private final GitHubRepoRepository gitHubRepoRepository;
    private final GitHubIssueRepository gitHubIssueRepository;

    public PostService(PostRepository postRepository,UserService userService,GitHubRepoRepository gitHubRepoRepository,GitHubIssueRepository gitHubIssueRepository) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.gitHubRepoRepository = gitHubRepoRepository;
        this.gitHubIssueRepository = gitHubIssueRepository;
    }

    public PostDto createPost(PostRequestDto postRequestDto, Principal principal) {

        User author = getUserFromPrincipal(principal);
        GitHubRepo gitHubRepo = null;
        GitHubIssue gitHubIssue = null;

        // Validate GitHub Repository
        if (postRequestDto.getRepoId() != null) {
            gitHubRepo = gitHubRepoRepository.findByGithubId(postRequestDto.getRepoId());
            if (gitHubRepo == null) {
                throw new ResourceNotFoundException("GitHub repository not found with ID: " + postRequestDto.getRepoId());
            }
        }

        // Validate GitHub Issue
        if (postRequestDto.getIssueId() != null) {
            gitHubIssue = gitHubIssueRepository.findByGitHubId(postRequestDto.getIssueId());
            if (gitHubIssue == null) {
                throw new ResourceNotFoundException("GitHub issue not found with ID: " + postRequestDto.getIssueId());
            }
        }

        // Create and save the post
        Post post = Mapper.toPostEntity(postRequestDto, author, gitHubRepo, gitHubIssue);
        Post savedPost = postRepository.save(post);

        return Mapper.toPostDTO(savedPost);
    }


    public PostDto updatePost(Long id, Post updatedPost) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setTags(updatedPost.getTags());

        return Mapper.toPostDTO(existingPost);
    }

    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }


    public List<PostDto> getPosts(boolean onlyPublished) {
        List<Post> posts;
        if (onlyPublished) {
            posts = postRepository.findByPublishedTrue();
        } else {
            posts = postRepository.findAll();
        }
        // Map to PostDTO
        return posts.stream()
                .map(Mapper::toPostDTO)
                .collect(Collectors.toList());
    }


    public PostDto saveDraft(PostRequestDto postRequestDto, Principal principal) {
        User author = getUserFromPrincipal(principal);
        GitHubRepo gitHubRepo = null;
        GitHubIssue gitHubIssue = null;

        if (postRequestDto.getRepoId() != null) {
            gitHubRepo = gitHubRepoRepository.findByGithubId(postRequestDto.getRepoId());
            if (gitHubRepo == null) {
                throw new RuntimeException("GitHub repository not found");
            }
        }

        if(postRequestDto.getIssueId() != null){
            gitHubIssue = gitHubIssueRepository.findByGitHubId(postRequestDto.getIssueId());
            if(gitHubIssue == null){
                throw new RuntimeException("GitHub Issue not found");
            }
        }

        // Create a Post object and set the author and other properties
        Post post = Mapper.toPostEntity(postRequestDto, author,gitHubRepo,gitHubIssue);
        post.setPublished(false); // Drafts are not published

        // Save the draft
        Post savedDraft = postRepository.save(post);

        // Return the saved draft as PostDto
        return Mapper.toPostDTO(savedDraft);
    }


    public List<PostDto> getDraftsByAuthor(Principal principal) {
        User author = userService.getUserByEmail(principal.getName()); // Fetch the user from the Principal
        List<Post> drafts = postRepository.findByAuthorAndPublishedFalse(author);

        // Convert the Post entities to PostDTOs
        return drafts.stream()
                .map(Mapper::toPostDTO) // Call the static method from PostMapper
                .collect(Collectors.toList());
    }

    public PostDto updateDraft(Long id, Post post) {
        // Logic to update the draft
        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setTitle(post.getTitle());
                    existingPost.setContent(post.getContent());
                    existingPost.setTags(post.getTags());
                    postRepository.save(existingPost);
                    return Mapper.toPostDTO(existingPost);
                }).orElseThrow(() -> new ResourceNotFoundException("Draft not found"));
    }

    public void deleteDraft(Long id) {
        postRepository.deleteById(id);
    }

    public PostDto publishDraft(Long id, Principal principal) {
        return postRepository.findById(id)
                .map(existingPost -> {
                    existingPost.setPublished(true);
                    postRepository.save(existingPost);
                    return Mapper.toPostDTO(existingPost);
                }).orElseThrow(() -> new ResourceNotFoundException("Draft not found"));
    }


    private User getUserFromPrincipal(Principal principal) {
        String email = principal.getName();
        return userService.getUserByEmail(email);
    }

    public boolean isAuthor(Long id, UserDetails userDetails) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        User author = post.getAuthor(); // Assuming Post entity has a reference to its author
        return author.getEmail().equals(userDetails.getUsername());
    }
}
