package com.ott.ott_server.dto.user.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@ApiModel(value = "로그인 요청 정보")
public class UserLoginRequestData {

    @NotBlank
    @ApiModelProperty(value = "가입된 사용자의 아이디", required = true, example = "kmw10693")
    private String userId;

    @NotBlank
    @ApiModelProperty(value = "가입된 사용자의 비밀번호", required = true, example = "test1234")
    private String password;

}
