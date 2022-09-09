package com.ott.ott_server.dto.user.response;

import com.ott.ott_server.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSocialRegistrationData {
    private String email;
    private String nickname;
    private Long birth;
    private String provider;

    public User toEntity() {
        return User.builder()
                .email(email)
                .nickname(nickname)
                .provider(provider)
                .birth(birth)
                .roles(Collections.singletonList("ROLE_USER"))
                .build();
    }
}
