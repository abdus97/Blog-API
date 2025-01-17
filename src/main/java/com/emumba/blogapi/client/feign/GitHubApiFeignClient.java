package com.emumba.blogapi.client.feign;

import com.emumba.blogapi.client.model.GitHubIssue;
import com.emumba.blogapi.client.model.GitHubUser;
import com.emumba.blogapi.client.model.GitHubRepo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "githubApi", url = "https://api.github.com")
public interface GitHubApiFeignClient {
    @GetMapping("/user")
    GitHubUser fetchUserInfo(@RequestHeader(HttpHeaders.AUTHORIZATION) String authorization);
    @GetMapping("/user/repos")
    List<GitHubRepo> getUserRepositories(@RequestHeader("Authorization") String authHeader);
    @GetMapping("/repos/{owner}/{repo}/issues")
    List<GitHubIssue> getRepositoryIssues(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable("owner") String owner,
            @PathVariable("repo") String repo);
}
