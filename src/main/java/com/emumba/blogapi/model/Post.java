package com.emumba.blogapi.model;

import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubRepo;
import com.emumba.blogapi.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    private boolean published = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ElementCollection
    @CollectionTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>(); // Optional tags for the post

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "github_repo_id", nullable = true) // Foreign key to GitHubRepo
    private GitHubRepo githubRepo;
    // Additional fields can be added as needed (e.g., created_at, updated_at, etc.)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "github_issue_id", nullable = true) // Foreign key to GitHubRepo
    private GitHubIssue githubIssue;
}