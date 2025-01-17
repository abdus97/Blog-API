package com.emumba.blogapi.client.controller;

import com.emumba.blogapi.client.dto.GitHubIssueResponse;
import com.emumba.blogapi.client.dto.GitHubRepoResponse;
import com.emumba.blogapi.client.helper.RepositoryControllerHelper;
import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubRepo;
import com.emumba.blogapi.client.repository.GitHubIssueRepository;
import com.emumba.blogapi.client.repository.GitHubRepoRepository;
import com.emumba.blogapi.client.service.RepositoryService;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.repository.UserRepository;
import com.emumba.blogapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/repositories")
public class RepositoryController {

    private final RepositoryService repositoryService;
    private final RepositoryControllerHelper repositoryControllerHelper;
    private final GitHubRepoRepository gitHubRepoRepository;
    private final GitHubIssueRepository gitHubIssueRepository;

    @Autowired
    public RepositoryController(RepositoryService repositoryService,
                                RepositoryControllerHelper repositoryControllerHelper,
                                GitHubRepoRepository gitHubRepoRepository,
                                GitHubIssueRepository gitHubIssueRepository) {
        this.repositoryService = repositoryService;
        this.repositoryControllerHelper = repositoryControllerHelper;
        this.gitHubRepoRepository = gitHubRepoRepository;
        this.gitHubIssueRepository = gitHubIssueRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READER')")
    public List<GitHubRepoResponse> getUserRepositories(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        User user = validateUserAndGetToken(authorizationHeader);
        List<GitHubRepo> fetchedRepositories = repositoryService.fetchUserRepositories(user.getAccessToken());
        List<GitHubRepo> processedRepositories = repositoryControllerHelper.processRepositories(fetchedRepositories, user);
        gitHubRepoRepository.saveAll(processedRepositories);
        return convertToRepoResponses(processedRepositories);
    }

    @GetMapping("/{githubRepoId}/issues")
    @PreAuthorize("hasAuthority('READER')")
    public List<GitHubIssueResponse> getRepositoryIssues(
            @PathVariable Long githubRepoId,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        User user = validateUserAndGetToken(authorizationHeader);
        GitHubRepo gitHubRepo = findGitHubRepoById(githubRepoId);

        List<GitHubIssue> fetchedIssues = repositoryService.fetchRepositoryIssues(user.getAccessToken(), gitHubRepo);
        List<GitHubIssue> processedIssues = repositoryControllerHelper.processGitHubIssue(fetchedIssues, gitHubRepo);
        gitHubIssueRepository.saveAll(processedIssues);
        return convertToIssueResponses(processedIssues);
    }

    private User validateUserAndGetToken(String authorizationHeader) {
        User user = repositoryControllerHelper.validateAndExtractUser(authorizationHeader);
        if (user.getAccessToken() == null) {
            throw new RuntimeException("GitHub access token not found for user");
        }
        return user;
    }

    private GitHubRepo findGitHubRepoById(Long githubRepoId) {
        GitHubRepo gitHubRepo = gitHubRepoRepository.findByGithubId(githubRepoId);
        if (gitHubRepo == null) {
            throw new RuntimeException("Repository not found");
        }
        return gitHubRepo;
    }

    private List<GitHubRepoResponse> convertToRepoResponses(List<GitHubRepo> repositories) {
        return repositories.stream()
                .map(repositoryControllerHelper::convertToResponse)
                .toList();
    }

    private List<GitHubIssueResponse> convertToIssueResponses(List<GitHubIssue> issues) {
        return issues.stream()
                .map(repositoryControllerHelper::convertToResponse)
                .toList();
    }
}

