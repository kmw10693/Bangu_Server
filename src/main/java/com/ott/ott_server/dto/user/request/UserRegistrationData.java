package com.ott.ott_server.dto.user.request;

import com.github.dozermapper.core.Mapping;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter
@AllArgsConstructor
@Builder
@Setter
@ApiModel(value = "유저 등록 요청 정보")
public class UserRegistrationData {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("email")
    @ApiModelProperty(value = "사용자 이메일", required = true, example = "bangu123")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$",
            message = "아이디는 영문 대,소문자와 숫자1개 이상씩 포함된 3자 이상의 아이디여야 합니다.")
    private String userId;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("password")
    @ApiModelProperty(value = "사용자 비밀번호", required = true, example = "test123")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$",
            message = "비밀번호는 영문 대,소문자와 숫자1개 이상씩 포함된 3자 이상의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max=8)
    @Mapping("nickname")
    @ApiModelProperty(value = "사용자 닉네임", required = true, example = "test")
    private String nickname;

    @NotNull(message = "생년월일을 입력해주세요.")
    @Mapping("birth")
    @ApiModelProperty(value = "사용자 생년월일", required = true, example = "20010320")
    private String birth;

    @ApiModelProperty(value = "Tving 구독 여부", required = true, example = "true")
    private boolean tving;

    @ApiModelProperty(value = "Watcha 구독 여부", required = true, example = "true")
    private boolean watcha;

    @ApiModelProperty(value = "Netflix 구독 여부", required = true, example = "true")
    private boolean netflix;

    @ApiModelProperty(value = "Wavve 구독 여부", required = true, example = "true")
    private boolean wavve;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public UserRegistrationData() {
        tving = false;
        watcha = false;
        netflix = false;
        wavve = false;
    }

    public User toEntity() {
        return User.builder()
                .email(userId)
                .password(password)
                .nickname(nickname)
                .gender(gender)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

}
