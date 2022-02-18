package com.ott.ott_server.controllers;

import com.ott.ott_server.application.AuthenticationService;
import com.ott.ott_server.dto.TokenResponseData;
import com.ott.ott_server.dto.UserLoginData;
import com.ott.ott_server.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sessions")
@RequiredArgsConstructor
public class SessionController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponseData> authorize(@RequestBody UserLoginData loginData) {

        TokenResponseData tokenResponseData = authenticationService.login(loginData);

        // 1. Response Header에 token 값을 넣어준다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + tokenResponseData.getToken());

        // 2. Response Body에 token 값을 넣어준다.
        return new ResponseEntity<>(tokenResponseData, httpHeaders, HttpStatus.OK);
    }
}