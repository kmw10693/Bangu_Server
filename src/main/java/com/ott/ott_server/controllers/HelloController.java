package com.ott.ott_server.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/")
public class HelloController {

    @GetMapping
    @ApiIgnore
    public String sayHello() { return "Hello world!"; }
}
