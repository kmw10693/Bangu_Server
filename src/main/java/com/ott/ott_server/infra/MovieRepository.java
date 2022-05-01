package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByTitleContaining(String search);

    // 주어진 영화에 대한 리뷰들의 평점을 계산하여 가져옵니다.
    @Query("select avg(score) from Review where movie = :movie and deleted = false")
    BigDecimal calcScoreAvg(Movie movie);

    Page<Movie> findAllByDeletedFalse(Pageable pageable);

    List<Movie> findByGenreNameAndOttsOttNameAndTitleContainingAndDeletedIsFalse(String genre, String ott, String title);

}
