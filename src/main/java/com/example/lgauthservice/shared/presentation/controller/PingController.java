package com.example.lgauthservice.shared.presentation.controller;

import com.example.lgauthservice.shared.infrastructure.web.response.ApiResponse;
import com.example.lgauthservice.shared.infrastructure.web.response.ResponseFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ping")
public class PingController {
    private final ResponseFactory responseFactory;

    public PingController(ResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Void>> ping() {
        return responseFactory.ok(null);
    }
}
