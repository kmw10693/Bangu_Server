package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Movie findByTitleContainingAndDirector(String title, String director);

    Page<Movie> findAllByDeletedFalse(Pageable pageable);

    List<Movie> findByOttsOttNameAndTitleContainingAndDeletedIsFalse(String ott, String title);

    boolean existsByTitle(String title);

    Movie findByTitle(String title);

    Page<Movie> findAllByTitleContaining(String title, Pageable pageable);

}
