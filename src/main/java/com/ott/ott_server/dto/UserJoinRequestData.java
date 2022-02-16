package com.ott.ott_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@Builder
public class UserJoinRequestData {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min=3, max=20)
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min=3, max=20)
    private String password;

    @NotBlank(message = "비밀번호 확인을 입력해주세요.")
    private String verifyPassword;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max=8)
    private String nickname;

}
