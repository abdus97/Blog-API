package com.emumba.blogapi.client.repository;

import com.emumba.blogapi.client.model.GitHubIssue;

import com.emumba.blogapi.client.model.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GitHubIssueRepository extends JpaRepository<GitHubIssue, Long> {
    GitHubIssue findByGitHubId(Long gitHubId); // Ensure the method matches the property case
}

