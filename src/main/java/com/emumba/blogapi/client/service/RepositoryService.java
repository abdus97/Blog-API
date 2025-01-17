package com.emumba.blogapi.client.service;

import com.emumba.blogapi.client.feign.GitHubApiFeignClient;
import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubRepo;
import com.emumba.blogapi.client.repository.GitHubRepoRepository;
import com.emumba.blogapi.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryService {

    private final GitHubApiFeignClient gitHubApiClient;
    private final GitHubRepoRepository gitHubRepoRepository;

    @Autowired
    public RepositoryService(GitHubApiFeignClient gitHubApiClient, GitHubRepoRepository gitHubRepoRepository) {
        this.gitHubApiClient = gitHubApiClient;
        this.gitHubRepoRepository = gitHubRepoRepository;
    }

    public List<GitHubRepo> fetchUserRepositories(String token) {
        String authHeader = createAuthHeader(token);
        return gitHubApiClient.getUserRepositories(authHeader);
    }

    public List<GitHubIssue> fetchRepositoryIssues(String accessToken, GitHubRepo gitHubRepo) {
        validateGitHubRepo(gitHubRepo);

        String owner = gitHubRepo.getUser().getUsername();
        String repo = gitHubRepo.getName();

        return gitHubRepo.getIsPrivate() != null && gitHubRepo.getIsPrivate()
                ? gitHubApiClient.getRepositoryIssues(createAuthHeader(accessToken), owner, repo)
                : gitHubApiClient.getRepositoryIssues("", owner, repo);
    }

    private String createAuthHeader(String token) {
        return "Bearer " + token; // Creates the authorization header
    }

    private void validateGitHubRepo(GitHubRepo gitHubRepo) {
        if (gitHubRepo == null || gitHubRepo.getUser() == null || gitHubRepo.getName() == null) {
            throw new IllegalArgumentException("Invalid GitHub repository information.");
        }
    }
}
