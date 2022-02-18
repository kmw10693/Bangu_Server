package com.ott.ott_server.dto;

import com.github.dozermapper.core.Mapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@AllArgsConstructor
@Builder
public class UserModificationData {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("email")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("password")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max=8)
    @Mapping("nickname")
    private String nickname;

}
