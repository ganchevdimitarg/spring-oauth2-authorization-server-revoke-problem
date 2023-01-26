package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@RestController
@Slf4j
@RequiredArgsConstructor
public class DemoController {
    private final WebClient webClient;

    @GetMapping("/demo")
    public String demo(Authentication a) {
        return "Demo!";
    }

    @PostMapping("/demo/logout")
    public void logout(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("Authorization").replace("Bearer ", "");

        ResponseEntity<Void> revokeResponse = webClient
                .post()
                .uri("http://localhost:8444/oauth2/revoke?token=" + token)
                .headers(headers -> headers.setBasicAuth("client", "secret"))
                .retrieve()
                .toEntity(Void.class)
                .block();
        log.info("""
                Token: {}
                Status: {}
                """, token, revokeResponse.getStatusCode().value());
    }
}