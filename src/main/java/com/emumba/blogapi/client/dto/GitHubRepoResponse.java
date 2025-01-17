package com.emumba.blogapi.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoResponse {
    private Long githubId;
    private String name;
    private String description;
    private String htmlUrl;
}
