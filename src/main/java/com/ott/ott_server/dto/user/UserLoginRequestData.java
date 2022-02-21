package com.ott.ott_server.dto.user;

import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserLoginRequestData {

    @NotBlank
    @ApiParam(value = "가입된 사용자의 이메일", required = true, example = "test@email.com")
    private String email;

    @NotBlank
    @ApiParam(value = "가입된 사용자의 비밀번호", required = true, example = "test12345678")
    private String password;
}
