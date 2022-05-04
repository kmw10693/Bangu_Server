package com.ott.ott_server.domain;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookMark extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private Review review;

    @ManyToOne(fetch = LAZY)
    private User user;

    @Builder
    public BookMark(Review review, User user) {
        this.review = review;
        this.user = user;
    }

}
