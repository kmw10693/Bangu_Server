package com.ott.ott_server.domain;

import com.ott.ott_server.dto.user.response.UserOttResponseData;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOtt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ott_id")
    private Ott ott;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public UserOttResponseData toUserOttResponseData() {
        return UserOttResponseData.builder()
                .ottName(ott.getName())
                .ottId(ott.getId())
                .userId(user.getId())
                .build();
    }
}
