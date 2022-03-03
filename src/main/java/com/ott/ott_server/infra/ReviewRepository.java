package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.UserOtt;
import com.ott.ott_server.domain.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeletedIsFalse(Long id);

    List<Review> findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(String genre, String ott, String title);

    List<Review> findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndUserBirthAndUserGenderAndDeletedIsFalseOrderByIdDesc(String genre, String ott, String title, Long birth, Gender gender);

    List<Review> findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(String title, Long birth, Gender gender);

    List<Review> findByMovieTitleContainingAndDeletedFalseOrderByIdDesc(String title);

    List<Review> findAllByOttsOttInAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(List<Ott> otts, Long birth, Gender gender);

    List<Review> findAllByOttsOttInAndDeletedIsFalseOrderByIdDesc(List<Ott> otts);
}
