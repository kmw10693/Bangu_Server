package com.ott.ott_server.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "로그인 요청 정보")
public class UserLoginRequestData {

    @NotBlank
    @ApiParam(value = "가입된 사용자의 id", required = true, example = "kmw10693")
    private String userId;

    @NotBlank
    @ApiParam(value = "가입된 사용자의 비밀번호", required = true, example = "test12345678")
    private String password;
}
