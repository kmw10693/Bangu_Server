package com.ott.ott_server.domain;

import com.ott.ott_server.dto.movie.response.MovieOttResponseData;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @Builder
    public MovieOtt(Ott ott, Movie movie) {
        this.ott = ott;
        this.movie = movie;
    }

    public MovieOttResponseData toMovieOttResponseData() {
        return MovieOttResponseData.builder()
                .ottName(ott.getName())
                .ottId(ott.getId())
                .build();
    }

    public void setMovieOtt(Movie movie) {
        this.movie = movie;
        movie.getOtts().add(this);
    }

}
