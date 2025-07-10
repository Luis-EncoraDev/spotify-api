package com.spotify.api.controllers;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        return "<h1>Welcome, " + principal.getAttribute("display_name") + "!</h1>" +
                "<p>Email: " + principal.getAttribute("email") + "</p>" +
                "<p>ID: " + principal.getAttribute("id") + "</p>";
    }
}

