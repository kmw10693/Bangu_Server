package com.ott.ott_server.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping
    @PreAuthorize("permitAll()")
    public String sayHello() { return "Hello world!"; }
}
