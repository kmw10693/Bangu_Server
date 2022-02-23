package com.ott.ott_server.domain;

import com.ott.ott_server.dto.movie.MovieOttResponseData;
import com.ott.ott_server.dto.user.UserOttResponseData;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MovieOtt extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "ott_id")
    private Ott ott;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    public MovieOttResponseData toMovieOttResponseData() {
        return MovieOttResponseData.builder()
                .ottName(ott.getName())
                .ottId(ott.getId())
                .build();
    }

}
