package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findByTitleContainingAndDirector(String title, String director);

    Page<Movie> findAllByDeletedFalse(Pageable pageable);

    List<Movie> findByGenreNameAndOttsOttNameAndTitleContainingAndDeletedIsFalse(String genre, String ott, String title);

}
