package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.domain.MovieOtt;
import com.ott.ott_server.domain.Ott;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieOttRepository extends JpaRepository<MovieOtt, Long> {
    boolean existsByOttAndMovie(Ott ott, Movie movie);
}
