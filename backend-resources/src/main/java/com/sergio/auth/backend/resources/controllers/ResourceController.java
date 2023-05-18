package com.sergio.auth.backend.resources.controllers;

import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class ResourceController {

    /*@GetMapping("/messages")
    public Authentication index(Principal principal) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }*/

    @GetMapping("/messages")
    public String getMessages() {
        return "the protected messages";
    }
}
