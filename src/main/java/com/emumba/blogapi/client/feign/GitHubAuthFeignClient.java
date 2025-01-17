package com.emumba.blogapi.client.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "githubAuth", url = "https://github.com")
public interface GitHubAuthFeignClient {
    @PostMapping("/login/oauth/access_token")
    String getAccessToken(
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri

    );
}
