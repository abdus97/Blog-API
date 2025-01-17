package com.emumba.blogapi.client.model;

import com.emumba.blogapi.model.user.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "github_repositories")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for your GitHub repository entity

    @Column(name = "github_id", nullable = false, unique = true)
    private Long githubId; // The ID of the repository from GitHub

    @Column(nullable = false)
    private String name; // Name of the repository



    @Column(nullable = true)
    private String description; // Description of the repository

    @Column(nullable = true)
    private String htmlUrl; // URL to the repository on GitHub

    @Column(nullable = true)
    @JsonProperty("private")
    private Boolean isPrivate; // Indicates if the repository is private

    @ManyToOne(fetch = FetchType.LAZY) // Associate with the user who owns this repository
    @JoinColumn(name = "user_id", nullable = false) // Foreign key to the User entity
    private User user; // User who owns the repository


}

