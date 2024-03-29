package com.ott.ott_server.infra;

import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.User;
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

    // 장르, 영화 이름 별로 최신순 가져오기
    List<Review> findByMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(String title);

    List<Review> findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedIsFalseOrderByIdDesc(String title, Long birth, Gender gender);

    List<Review> findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(String title, Long birth, Gender gender);

    List<Review> findByMovieTitleContainingAndDeletedFalseOrderByIdDesc(String title);

    // 홈에서 불러오기
    List<Review> findByOttsOttInAndDeletedIsFalseAndRevealedIsTrueOrderByIdDesc(List<Ott> otts);

    // 유저의 리뷰 가져오기
    List<Review> findByUserOrderByIdDesc(User user);

    // 팔로우 하는 유저의 리뷰 가져오기
    List<Review> findByUserInAndDeletedFalseAndRevealedIsTrueOrderByIdDesc(List<User> users);

    // 유저 아이디의 삭제 안된 리뷰 가져오기
    Page<Review> findByUserIdAndDeletedFalseOrderByIdDesc(Long userId, Pageable pageable);

    // 본 계정이 작성한 리뷰 가져오기
    List<Review> findByUserAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(User user, String title);

    // 팔로우한 사람 리뷰 가져오기
    List<Review> findByUserInAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(List<User> users, String title);

}
