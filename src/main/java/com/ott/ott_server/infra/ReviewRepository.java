package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeletedIsFalse(Long id);

    Page<Review> findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(String genre, String ott, String title, Pageable pageable);

    Page<Review> findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndUserBirthAndUserGenderAndDeletedIsFalseOrderByIdDesc(String genre, String ott, String title, Long birth, Gender gender, Pageable pageable);

    List<Review> findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(String title, Long birth, Gender gender);

    List<Review> findByMovieTitleContainingAndDeletedFalseOrderByIdDesc(String title);

    Page<Review> findByOttsOttInAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(List<Ott> otts, Long birth, Gender gender, Pageable pageable);

    Page<Review> findByOttsOttInAndDeletedIsFalseOrderByIdDesc(List<Ott> otts, Pageable pageable);
}
