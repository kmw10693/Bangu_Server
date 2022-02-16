package com.ott.ott_server.controllers;

import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user-ids/{id}/exists")
    public ResponseEntity<boolean> checkIdDuplicate(@PathVariable String id) {

    }

}
