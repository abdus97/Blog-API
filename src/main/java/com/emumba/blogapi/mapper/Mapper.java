package com.emumba.blogapi.mapper;

import com.emumba.blogapi.client.dto.PostRequestDto;
import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubRepo;
import com.emumba.blogapi.dto.CommentDto;
import com.emumba.blogapi.dto.PostDto;
import com.emumba.blogapi.dto.UserPostResponseDto;
import com.emumba.blogapi.model.Comment;
import com.emumba.blogapi.model.Post;
import com.emumba.blogapi.model.user.User;

public class Mapper {
    // Convert Post entity to PostDto
    public static PostDto toPostDTO(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setTitle(post.getTitle());
        postDto.setContent(post.getContent());
        postDto.setPublished(post.isPublished());
        postDto.setTags(post.getTags());

        if (post.getGithubRepo() != null) {
            postDto.setRepoId(post.getGithubRepo().getGithubId());
            postDto.setRepoName(post.getGithubRepo().getName());
        }

        if(post.getGithubIssue() != null){
            postDto.setIssueId(post.getGithubIssue().getId());
            postDto.setIssueTitle(post.getGithubIssue().getTitle());
        }


        return postDto;
    }

    // Convert PostRequestDto to Post entity (if needed for specific use cases)
    public static Post toPostEntity(PostRequestDto postRequestDto, User author, GitHubRepo githubRepo, GitHubIssue gitHubIssue) {
        Post post = new Post();
        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());
        post.setTags(postRequestDto.getTags());
        post.setPublished(postRequestDto.isPublished());
        post.setAuthor(author);
        post.setGithubRepo(githubRepo);
        post.setGithubIssue(gitHubIssue);
        return post;
    }

    public static CommentDto toCommentDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setAuthorName(comment.getUser().getUsername()); // Assuming User has a username field
        dto.setPostId(comment.getPost().getId());
        dto.setParentId(comment.getParent() != null ? comment.getParent().getId() : null);
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

}
