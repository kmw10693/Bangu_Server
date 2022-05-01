package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByIdAndDeletedIsFalse(Long id);

    // 장르, 영화 이름 별로 최신순 가져오기
    Page<Review> findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(String genre, String ott, String title, Pageable pageable);

    Page<Review> findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndUserBirthAndUserGenderAndDeletedIsFalseOrderByIdDesc(String genre, String ott, String title, Long birth, Gender gender, Pageable pageable);

    List<Review> findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(String title, Long birth, Gender gender);

    List<Review> findByMovieTitleContainingAndDeletedFalseOrderByIdDesc(String title);

    Page<Review> findByOttsOttInAndDeletedIsFalseOrderByIdDesc(List<Ott> otts, Pageable pageable);

    // 유저의 리뷰 가져오기
    Page<Review> findByUserOrderByIdDesc(User user, Pageable pageable);

    // 팔로우 하는 유저의 리뷰 가져오기
    Page<Review> findByUserInAndDeletedFalseOrderByIdDesc(List<User> users, Pageable pageable);

    // 유저 아이디의 삭제 안된 리뷰 가져오기
    Page<Review> findByUserIdAndDeletedFalseOrderByIdDesc(Long userId, Pageable pageable);

    // 본 계정이 작성한 리뷰 가져오기
    Page<Review> findByUserAndMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(User user, String genre, String ott, String title, Pageable pageable);

    // 팔로우한 사람 리뷰 가져오기
    Page<Review> findByUserInAndMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(List<User> users, String genre, String ott, String title, Pageable pageable);

}
