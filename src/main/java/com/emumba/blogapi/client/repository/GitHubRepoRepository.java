package com.emumba.blogapi.client.repository;

import com.emumba.blogapi.client.model.GitHubRepo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GitHubRepoRepository extends JpaRepository<GitHubRepo, Long> {

    GitHubRepo findByGithubId(Long githubId);

}
