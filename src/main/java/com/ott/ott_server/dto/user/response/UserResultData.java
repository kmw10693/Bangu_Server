package com.ott.ott_server.dto.user.response;

import com.github.dozermapper.core.Mapping;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.enums.Gender;
import com.ott.ott_server.dto.user.response.UserOttResponseData;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(value = "유저 등록 성공 정보")
public class UserResultData {
    @ApiModelProperty(value = "사용자 인덱스", example = "1")
    private Long id;

    @ApiModelProperty(value = "사용자 이메일", example = "test@email.com")
    private String email;

    @ApiModelProperty(value = "사용자 닉네임", example = "bangu")
    private String nickname;

    @ApiModelProperty(value = "나이대 ex) 20대 2", example = "2")
    private Long birth;

    @ApiModelProperty(value = "성별", example = "M")
    private Gender gender;

    @Builder.Default
    private List<UserOttResponseData> userOttResponseData = new ArrayList<>();

    @ApiModelProperty(value = "사용자 생성 시각", example = "2022-01-24T16:19:01.359146")
    private LocalDateTime create_at;

    @ApiModelProperty(value = "사용자 수정 시각", example = "2022-01-24T16:19:01.359146")
    private LocalDateTime update_at;

    @Getter
    @AllArgsConstructor
    @Builder
    @Setter
    @ApiModel(value = "유저 등록 요청 정보")
    public static class UserRegistrationData {
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
}
