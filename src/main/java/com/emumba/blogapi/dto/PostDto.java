package com.emumba.blogapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;


@Data
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private boolean published;
    private UserPostResponseDto author;
    private Set<String> tags;
    private Long repoId; // ID of the associated GitHub repository (if any)
    private String repoName;
    private Long issueId;
    private String issueTitle;

}