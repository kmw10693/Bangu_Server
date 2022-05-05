package com.ott.ott_server.domain;

import com.ott.ott_server.dto.movie.MovieListRes;
import com.ott.ott_server.dto.movie.MovieResponseData;
import lombok.*;
import org.hibernate.metamodel.model.domain.spi.MapPersistentAttribute;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Movie extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    private List<MovieOtt> otts = new ArrayList<>();

    @OneToMany(mappedBy = "movie", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    private String genre;

    private String title;

    private String imageUrl;

    private String director;

    private String actor;

    @Builder.Default
    private boolean deleted = false;

    public MovieResponseData toMovieResponseData() {
        return MovieResponseData.builder()
                .imageUrl(imageUrl)
                .title(title)
                .genre(genre)
                .movieOtts(otts.stream().map(MovieOtt::toMovieOttResponseData).collect(Collectors.toList()))
                .build();
    }

    public MovieListRes toMovieListRes() {
        return MovieListRes.builder()
                .movieId(id)
                .imageUrl(imageUrl)
                .build();
    }

    public void setOtts(MovieOtt movieOtt) {
        movieOtt.setMovieOtt(this);
        otts.add(movieOtt);
    }

}
