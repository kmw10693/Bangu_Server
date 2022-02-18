package com.ott.ott_server.domain;

import com.ott.ott_server.dto.UserResultData;
import lombok.*;

import javax.persistence.*;

import static com.ott.ott_server.domain.Role.ROLE_USER;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = ROLE_USER;

    @Builder.Default
    private boolean deleted = false;

    @Builder.Default
    private String imageUrl = "";

    public UserResultData toUserResultData() {
        return UserResultData.builder()
                .id(id)
                .create_at(getCreateAt())
                .update_at(getUpdateAt())
                .nickname(nickname)
                .email(email)
                .build();
    }

    public void changeWith(User source) {
        nickname = source.getNickname();
        imageUrl = source.getImageUrl();
        password = source.getPassword();
    }

    public void destroy() {
        deleted = true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Builder
    public User(String email, String password, String name, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

}
