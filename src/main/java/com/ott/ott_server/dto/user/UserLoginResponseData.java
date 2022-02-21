package com.ott.ott_server.dto.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class UserLoginResponseData {

    @ApiModelProperty(value = "사용자 인덱스", example = "1")
    private Long id;

    @ApiModelProperty(value = "사용자 아이디", example = "test123")
    private String email;

    @ApiModelProperty(value = "사용자 닉네임", example = "반구반구")
    private String nickname;

    @ApiModelProperty(value = "사용자 권한", example = "ROLE_USER")
    private List<String> roles;

    @ApiModelProperty(value = "사용자 생성 시각", example = "2022-01-24T16:19:01.359146")
    private LocalDateTime createAt;

    @ApiModelProperty(value = "사용자 수정 시각", example = "2022-01-24T16:19:01.359146")
    private LocalDateTime updateAt;

}