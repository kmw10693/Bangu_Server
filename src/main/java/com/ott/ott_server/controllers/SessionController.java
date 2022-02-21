package com.ott.ott_server.controllers;

import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.user.UserLoginRequestData;
import com.ott.ott_server.dto.user.UserLoginResponseData;
import com.ott.ott_server.dto.user.UserRegistrationData;
import com.ott.ott_server.dto.user.UserResultData;
import com.ott.ott_server.provider.JwtProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/session")
public class SessionController {

    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @ApiOperation(value = "로그인", notes = "아이디로 로그인을 합니다.")
    @GetMapping("/login")
    public String login(@RequestBody UserLoginRequestData userLoginRequestData) {
        String email = userLoginRequestData.getEmail();
        String password = userLoginRequestData.getPassword();
        UserLoginResponseData userLoginData = userService.login(email, password);

        String token = jwtProvider.createToken(String.valueOf(userLoginData.getId()), userLoginData.getRoles());
        return token;
    }

    @ApiOperation(value = "회원가입", notes = "회원가입을 합니다.")
    @PostMapping("/signup")
    public UserResultData signup(
            @RequestBody @Valid UserRegistrationData userRegistrationData) {
        userRegistrationData.setPassword(passwordEncoder.encode(userRegistrationData.getPassword()));
        User user = userService.signup(userRegistrationData);
        return user.toUserResultData();
    }

}