package com.ott.ott_server.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOtt extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "ott_id")
    @ManyToOne(fetch = LAZY)
    private Ott ott;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private User user;

}
