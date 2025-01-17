package com.emumba.blogapi.client.controller;


import com.emumba.blogapi.client.service.GitHubService;
import com.emumba.blogapi.client.dto.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/github")
public class GitHubOAuthController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubOAuthController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping("/login")
    public ResponseEntity<String> getGitHubLoginUrl() {
        String loginUrl = gitHubService.getGitHubLoginUrl();
        return ResponseEntity.ok(loginUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> handleGitHubCallback(@RequestParam("code") String code) {
        UserToken userToken = gitHubService.getUserToken(code);
        return ResponseEntity.ok("User logged in or signed up successfully: " + userToken.getUser().getUsername() + ". JWT: " + userToken.getToken());
    }






}
