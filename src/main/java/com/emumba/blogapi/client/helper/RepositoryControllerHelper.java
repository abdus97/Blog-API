package com.emumba.blogapi.client.helper;

import com.emumba.blogapi.client.dto.GitHubIssueResponse;
import com.emumba.blogapi.client.dto.GitHubRepoResponse;
import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubRepo;
import com.emumba.blogapi.client.repository.GitHubIssueRepository;
import com.emumba.blogapi.client.repository.GitHubRepoRepository;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.repository.UserRepository;
import com.emumba.blogapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RepositoryControllerHelper {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GitHubRepoRepository gitHubRepoRepository;

    @Autowired
    GitHubIssueRepository gitHubIssueRepository;


    public List<GitHubRepo> processRepositories(List<GitHubRepo> repositories, User user) {
        return repositories.stream()
                .map(repo -> {
                    // Check if the repository already exists in the database
                    GitHubRepo existingRepo = gitHubRepoRepository.findByGithubId(repo.getId());
                    if (existingRepo != null) {
                        // Update existing repository
                        existingRepo.setName(repo.getName());
                        existingRepo.setDescription(repo.getDescription());
                        existingRepo.setHtmlUrl(repo.getHtmlUrl());
                        return existingRepo;
                    } else {
                        // Prepare a new repository for saving
                        repo.setGithubId(repo.getId());
                        repo.setId(null); // Ensure the new entity doesn't conflict with database IDs
                        repo.setUser(user);
                        return repo;
                    }
                })
                .toList();
    }

    // Helper method to convert GitHubRepo to GitHubRepoResponse
    public GitHubRepoResponse convertToResponse(GitHubRepo repo) {
        GitHubRepoResponse response = new GitHubRepoResponse();
        response.setGithubId(repo.getGithubId());
        response.setName(repo.getName());
        response.setDescription(repo.getDescription());
        response.setHtmlUrl(repo.getHtmlUrl());
        return response;
    }

    public GitHubIssueResponse convertToResponse(GitHubIssue issue) {
        GitHubIssueResponse response = new GitHubIssueResponse();
        response.setBody(issue.getBody());
        response.setTitle(issue.getTitle());
        response.setRepositoryUrl(issue.getRepositoryUrl());
        response.setHtmlUrl(issue.getHtmlUrl());
        response.setGitHubId(issue.getGitHubId());
        return response;
    }



    public User validateAndExtractUser(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header is missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    public List<GitHubIssue> processGitHubIssue(List<GitHubIssue> issues, GitHubRepo repo) {
        return issues.stream()
                .map(issue -> {
                    // Check if the repository already exists in the database
                    GitHubIssue existingIssue = gitHubIssueRepository.findByGitHubId(issue.getId());
                    if (existingIssue != null) {
                        // Update existing repository
                        existingIssue.setBody(issue.getBody());
                        existingIssue.setTitle(issue.getTitle());
                        existingIssue.setHtmlUrl(issue.getHtmlUrl());
                        existingIssue.setRepositoryUrl(issue.getRepositoryUrl());
                        return existingIssue;
                    } else {
                        // Prepare a new repository for saving
                        issue.setGitHubId(issue.getId());
                        issue.setId(null); // Ensure the new entity doesn't conflict with database IDs
                        issue.setGitHubRepo(repo);
                        return issue;
                    }
                })
                .toList();
    }

}
