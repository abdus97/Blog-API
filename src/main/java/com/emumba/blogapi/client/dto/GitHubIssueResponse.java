package com.emumba.blogapi.client.dto;

import lombok.Data;

@Data
public class GitHubIssueResponse {

    private Long gitHubId;
    private String repositoryUrl;
    private String htmlUrl;
    private String body;
    private String title;
    private Long Id;
}
