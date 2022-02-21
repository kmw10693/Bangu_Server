package com.ott.ott_server.dto.user;

import com.github.dozermapper.core.Mapping;
import com.ott.ott_server.domain.BaseTimeEntity;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.enums.Gender;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collections;

@Getter
@AllArgsConstructor
@Builder
@Setter
public class UserRegistrationData extends BaseTimeEntity {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("email")
    @ApiParam(value = "사용자 이메일", required = true, example = "test@email.com")
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("password")
    @ApiParam(value = "사용자 비밀번호", required = true, example = "test123")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max=8)
    @Mapping("nickname")
    @ApiParam(value = "사용자 닉네임", required = true, example = "test")
    private String nickname;

    @NotBlank(message = "생년월일을 입력해주세요.")
    @Mapping("birth")
    private String birth;

    // 구독하는 OTT
    @Builder.Default
    @ApiParam(value = "tving 구독 여부", required = true, example = "true")
    private boolean tving = true;

    @Builder.Default
    @ApiParam(value = "watcha 구독 여부", required = true, example = "true")
    private boolean watcha = true;

    @Builder.Default
    @ApiParam(value = "netflix 구독 여부", required = true, example = "true")
    private boolean netflix = true;

    @Builder.Default
    @ApiParam(value = "wavve 구독 여부", required = true, example = "true")
    private boolean wavve = true;

    @Enumerated(EnumType.STRING)
    @Column(length = 1)
    private Gender gender;

    public User toEntity() {
        return User.builder()
                .email(email)
                .password(password)
                .nickname(nickname)
                .birth(birth)
                .gender(gender)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }

}
