package com.ott.ott_server.domain;

import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.dto.review.ReviewModificationData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import lombok.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.List;

import static javax.persistence.FetchType.LAZY;


@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private String attention;

    private String dialogue;

    private String content;

    private BigDecimal score;

    @Builder.Default
    private boolean revealed = true;

    @Builder.Default
    private boolean deleted = false;

    public ReviewResponseData toReviewResponseData() {

        return ReviewResponseData.builder()
                .id(id)
                .attention(attention)
                .movieResponseData(movie.toMovieResponseData())
                .dialogue(dialogue)
                .content(content)
                .userId(user.getId())
                .score(score)
                .build();
    }

    public void update(ReviewModificationData reviewModificationData){
        this.revealed = reviewModificationData.isRevealed();
        this.dialogue = reviewModificationData.getDialogue();
        this.content = reviewModificationData.getContent();
        this.attention = reviewModificationData.getAttention();
        this.score = reviewModificationData.getScore();
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
