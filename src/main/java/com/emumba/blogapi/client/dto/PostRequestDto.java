package com.emumba.blogapi.client.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PostRequestDto {
    private String title; // Title of the post
    private String content; // Content of the post
    private boolean published = false; // Whether the post is published
    private Set<String> tags; // Optional tags for the post
    private Long repoId;
    private Long issueId;
}
