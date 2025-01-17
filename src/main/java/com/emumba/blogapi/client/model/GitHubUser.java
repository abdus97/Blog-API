package com.emumba.blogapi.client.model;

import lombok.Data;

@Data
public class GitHubUser {
    private Long id;
    private String login;
    private String name;
    private String email;
    private String avatar_url; // URL to the user's avatar image
    private String html_url; // URL to the user's GitHub profile

    // You can add other fields as needed based on the GitHub API response
}