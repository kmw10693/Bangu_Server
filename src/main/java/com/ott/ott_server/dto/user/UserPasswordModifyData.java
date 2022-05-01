package com.ott.ott_server.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ApiModel("비밀번호 수정 요청 정보")
public class UserPasswordModifyData {

    @Size(min=3, max=20)
    @ApiModelProperty(value = "수정할 비밀번호", example = "1")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$",
            message = "비밀번호는 영문 대,소문자와 숫자1개 이상씩 포함된 3자 이상의 아이디여야 합니다.")
    private String password;
}

