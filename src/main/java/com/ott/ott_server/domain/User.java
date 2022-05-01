package com.ott.ott_server.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ott.ott_server.domain.enums.Gender;
import com.ott.ott_server.dto.user.UserLoginResponseData;
import com.ott.ott_server.dto.user.UserProfileData;
import com.ott.ott_server.dto.user.UserResultData;
import com.ott.ott_server.dto.user.UserSignupResponseData;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String nickname;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Column(length = 100) // provider 추가 (kakao, naver, google etc.)
    private String provider;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserOtt> userOtt = new ArrayList<>();

    private Long birth;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private String imageUrl = "";

    public void destroy() {
        deleted = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public String getUsername() {
        return String.valueOf(id);
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }

    public UserResultData toUserResultData() {
        return UserResultData.builder()
                .id(id)
                .birth(birth)
                .gender(gender)
                .userOttResponseData(userOtt.stream().map(UserOtt::toUserOttResponseData).collect(Collectors.toList()))
                .create_at(getCreateAt())
                .update_at(getUpdateAt())
                .nickname(nickname)
                .email(email)
                .build();
    }

    public UserSignupResponseData toUserSignupResponseData() {
        return UserSignupResponseData.builder()
                .id(id)
                .birth(birth)
                .gender(gender)
                .create_at(getCreateAt())
                .update_at(getUpdateAt())
                .nickname(nickname)
                .email(email)
                .build();
    }

    public UserLoginResponseData toUserLoginResponseData() {
        return UserLoginResponseData.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .roles(roles)
                .createAt(getCreateAt())
                .updateAt(getUpdateAt())
                .build();
    }

    public UserProfileData toUserProfileData() {
        return UserProfileData.builder()
                .id(id)
                .birth(birth)
                .gender(gender)
                .imageUrl(imageUrl)
                .nickname(nickname)
                .build();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBirth(Long birth) {
        this.birth = birth;
    }

}
