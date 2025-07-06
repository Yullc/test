package org.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GitHubLoginController {


    private String clientId;


    private String redirectUri;

    @GetMapping("/login/github")
    public String redirectToGithub() {
        String githubAuthUrl = "https://github.com/login/oauth/authorize" +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&scope=user:email" +
                "&prompt=login";

        return "redirect:" + githubAuthUrl;
    }

    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "redirect:/";
    }
}
