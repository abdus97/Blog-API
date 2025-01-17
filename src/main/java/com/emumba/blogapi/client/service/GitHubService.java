package com.emumba.blogapi.client.service;

import com.emumba.blogapi.client.feign.GitHubApiFeignClient;
import com.emumba.blogapi.client.feign.GitHubAuthFeignClient;
import com.emumba.blogapi.client.dto.UserToken;
import com.emumba.blogapi.client.model.GitHubUser;
import com.emumba.blogapi.model.user.Role;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.repository.RoleRepository;
import com.emumba.blogapi.repository.UserRepository;
import com.emumba.blogapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Service
public class GitHubService {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String scope;
    private final GitHubAuthFeignClient gitHubAuthClient;
    private final GitHubApiFeignClient gitHubApiClient;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RoleRepository roleRepository;

    @Autowired
    public GitHubService(
            @Value("${github.client.id}") String clientId,
            @Value("${github.client.secret}") String clientSecret,
            @Value("${github.redirect.uri}") String redirectUri,
            @Value("${github.scope}") String scope,
            GitHubAuthFeignClient gitHubAuthClient,
            GitHubApiFeignClient gitHubApiClient,
            UserRepository userRepository,
            JwtUtil jwtUtil,
            RoleRepository roleRepository) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.gitHubAuthClient = gitHubAuthClient;
        this.gitHubApiClient = gitHubApiClient;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.scope = scope;
    }

    public String getGitHubLoginUrl() {
        return String.format("https://github.com/login/oauth/authorize?client_id=%s&redirect_uri=%s&scope=%s",
                clientId, redirectUri, scope);
    }

    public String getAccessToken(String code) {
        String response = gitHubAuthClient.getAccessToken(clientId, clientSecret, code, redirectUri);
        return extractAccessToken(response);
    }

    private String extractAccessToken(String response) {
        String[] params = response.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("access_token") && keyValue.length > 1) {
                return keyValue[1];
            }
        }
        throw new RuntimeException("Access token not found in response: " + response);
    }

    public GitHubUser fetchUserInfo(String accessToken) {
        return gitHubApiClient.fetchUserInfo("Bearer " + accessToken);
    }

    public User getUser(GitHubUser gitHubUser, String accessToken) {
        return userRepository.findByGithubId(gitHubUser.getId())
                .orElseGet(() -> createNewUser(gitHubUser, accessToken));
    }

    private User createNewUser(GitHubUser gitHubUser, String accessToken) {
        User newUser = new User();
        newUser.setUsername(gitHubUser.getLogin());
        newUser.setEmail(gitHubUser.getLogin());
        newUser.setIsGithubUser(true);
        newUser.setGithubId(gitHubUser.getId());
        newUser.setAccessToken(accessToken);
        newUser.setRoles(getDefaultRoles());

        return userRepository.save(newUser);
    }

    private Set<Role> getDefaultRoles() {
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName("AUTHOR"));
        roles.add(roleRepository.findByName("READER"));
        return roles;
    }

    public UserToken getUserToken(String code) {
        String accessToken = getAccessToken(code);
        GitHubUser gitHubUser = fetchUserInfo(accessToken);
        User user = getUser(gitHubUser, accessToken);

        return generateUserToken(user);
    }

    private UserToken generateUserToken(User user) {
        UserDetails userDetails = convertToUserDetails(user);
        String jwtToken = jwtUtil.generateToken(userDetails);
        return new UserToken(user, jwtToken);
    }

    private UserDetails convertToUserDetails(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail() != null ? user.getEmail() : user.getUsername(),
                user.getPassword() != null ? user.getPassword() : user.getEmail() + "Password",
                authorities
        );
    }
}

