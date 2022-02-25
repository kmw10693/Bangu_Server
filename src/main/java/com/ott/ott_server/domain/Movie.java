package com.ott.ott_server.domain;

import com.ott.ott_server.dto.movie.MovieResponseData;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.metamodel.model.domain.spi.MapPersistentAttribute;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static javax.persistence.FetchType.LAZY;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Movie extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "movie")
    @Builder.Default
    private List<MovieOtt> otts = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private String title;

    private String imageUrl;

    private String director;

    private String actor;

    private String birth;

    @Builder.Default
    private boolean deleted = false;

    public MovieResponseData toMovieResponseData() {

        return MovieResponseData.builder()
                .id(id)
                .actor(actor)
                .director(director)
                .imageUrl(imageUrl)
                .title(title)
                .birth(birth)
                .deleted(deleted)
                .genre(genre.getName())
                .movieOtts(otts.stream().map(MovieOtt::toMovieOttResponseData).collect(Collectors.toList()))
                .build();
    }

}
