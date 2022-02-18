package com.ott.ott_server.controllers;

import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.UserModificationData;
import com.ott.ott_server.dto.UserRegistrationData;
import com.ott.ott_server.dto.UserResultData;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 유저 회원가입 API
     */
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResultData create(Authentication authentication, @RequestBody @Valid UserRegistrationData userRegistrationData) {

        User user = userService.registerUser(userRegistrationData);
        return user.toUserResultData();
    }

    /**
     * 사용자 정보 조회 API
     * @return
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResultData detail(@PathVariable("id") Long id) {
        User user = userService.getUser(id);
        return user.toUserResultData();
    }

    /**
     * 사용자 업데이트 API
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public UserResultData update(@PathVariable("id") Long id, @RequestBody @Valid UserModificationData modificationData) {
        User user = userService.updateUser(id, modificationData);
        return user.toUserResultData();
    }

    /**
     * 사용자 삭제 API
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

}
