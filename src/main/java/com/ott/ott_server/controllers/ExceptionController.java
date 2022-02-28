package com.ott.ott_server.controllers;

import com.ott.ott_server.errors.AccessDeniedException;
import com.ott.ott_server.errors.AuthenticationEntrypointException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/exception")
public class ExceptionController {
    @GetMapping("/entrypoint")
    public void entryPointException() {
        throw new AuthenticationEntrypointException();
    }
    @GetMapping("/accessDenied")
    public void accessDeniedException() {
        throw new AccessDeniedException();
    }
}
