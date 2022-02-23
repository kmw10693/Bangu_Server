package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeletedIsFalse(Long id);

    List<Review> findByMovieGenreNameAndMovieOttsOttName(String genre, String ott);
}
