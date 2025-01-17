package com.emumba.blogapi.client.model;


import com.emumba.blogapi.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_issues")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // GitHub issue ID

    @Column(nullable = false)
    private Long gitHubId;

    @Column(nullable = true)
    private String title; // Title of the issue

    @Column(nullable = true)
    @JsonProperty("body")
    private String body; // Description of the issue

    @Column(nullable = true)
    @JsonProperty("html_url")
    private String htmlUrl; // URL to the issue on GitHub

    @Column(nullable = true)
    @JsonProperty("repository_url")
    private String repositoryUrl;

    @ManyToOne(fetch = FetchType.LAZY) // Associate with the user who owns this repository
    @JoinColumn(name = "repo_id", nullable = false) // Foreign key to the User entity
    private GitHubRepo gitHubRepo; // User who owns the repository



}
