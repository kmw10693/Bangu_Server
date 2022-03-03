package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.*;
import com.ott.ott_server.domain.enums.Gender;
import com.ott.ott_server.dto.review.ReviewModificationData;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import com.ott.ott_server.dto.user.UserRegistrationData;
import com.ott.ott_server.errors.FollowAlreadyExistException;
import com.ott.ott_server.errors.OttNameNotFoundException;
import com.ott.ott_server.errors.ReviewNotFoundException;
import com.ott.ott_server.errors.UserNotMatchException;
import com.ott.ott_server.infra.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final OttRepository ottRepository;
    private final ReviewOttRepository reviewOttRepository;
    private final Mapper mapper;

    /**
     * 리뷰 생성
     *
     * @param movie
     * @param user
     * @param reviewRequestData
     * @return
     */
    public Review createReview(Movie movie, User user, ReviewRequestData reviewRequestData) {

        Review review = reviewRepository.save(
                Review.builder()
                        .user(user)
                        .attention(reviewRequestData.getAttention())
                        .content(reviewRequestData.getContent())
                        .dialogue(reviewRequestData.getDialogue())
                        .movie(movie)
                        .score(reviewRequestData.getScore())
                        .build()
        );
        checkSubscribe(reviewRequestData, review);
        return review;
    }

    private void checkSubscribe(ReviewRequestData reviewRequestData, Review review) {
        if (reviewRequestData.getReviewOtt().isNetflix()) {
            Ott ott = findIdByOttName("netflix");
            setReviewOtt(review, ott);
        }
        if (reviewRequestData.getReviewOtt().isTving()){
            Ott ott = findIdByOttName("tving");
            setReviewOtt(review, ott);
        }
        if(reviewRequestData.getReviewOtt().isWatcha()) {
            Ott ott = findIdByOttName("watcha");
            setReviewOtt(review, ott);
        }
        if(reviewRequestData.getReviewOtt().isWavve()) {
            Ott ott = findIdByOttName("wavve");
            setReviewOtt(review, ott);
        }

    }

    private Ott findIdByOttName(String title) {
        return ottRepository.findByNameContaining(title)
                .orElseThrow(OttNameNotFoundException::new);
    }

    private void setReviewOtt(Review review, Ott ott) {
        reviewOttRepository.save(ReviewOtt.builder()
                .review(review)
                .ott(ott)
                .build());
    }

    /**
     * 리뷰 인덱스로 찾기
     *
     * @param id
     * @return
     */
    public Review getReviewById(Long id) {
        return reviewRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }

    /**
     * ott와 genre 별로 리뷰 가져오기
     * @param ott
     * @param genre
     * @return
     */
    public List<ReviewResponseData> getReviews(User user, String ott, String genre, String title, Boolean sort) {

        Long birth = user.getBirth();
        Gender gender = user.getGender();
        List<Review> reviews;

        if(sort == true) {
            reviews = reviewRepository.findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndUserBirthAndUserGenderAndDeletedIsFalseOrderByIdDesc(genre, ott, title, birth, gender);
        } else {
            reviews = reviewRepository
                    .findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(genre, ott, title);
        }

        List<ReviewResponseData> reviewResponses = reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

        extracted(user, reviewResponses);
        return reviewResponses;
    }


    /**
     * 리뷰 업데이트
     *
     * @param reviewId
     * @param user
     * @param reviewModificationData
     * @return
     */
    public Review update(Long reviewId, User user, ReviewModificationData reviewModificationData) {
        Review review = getReviewById(reviewId);
        checkMatchUser(review, user.getId());
        review.update(reviewModificationData);
        return review;
    }

    /**
     * 리뷰 삭제하기
     *
     * @param reviewId
     * @param user
     */
    public void deleteReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
        checkMatchUser(review, user.getId());
        review.setDeleted(true);
    }

    /**
     * 리뷰 사용자 확인
     *
     * @param review
     * @param loginUserId
     */
    private void checkMatchUser(Review review, Long loginUserId) {
        if (review.getUser().getId() != loginUserId) {
            throw new UserNotMatchException();
        }
    }


    /**
     * 영화 제목으로 리뷰 찾기
     *
     * @param title
     * @return
     */
    public List<ReviewResponseData> findListByTitle(User user ,String title, Boolean sort) {

        Long birth = user.getBirth();
        Gender gender = user.getGender();
        List<Review> reviews;

        if(sort == true) {
            reviews = reviewRepository.findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(title, birth, gender);
        }else {
            reviews = reviewRepository.findByMovieTitleContainingAndDeletedFalseOrderByIdDesc(title);
        }
        List<ReviewResponseData> reviewDatas = reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

        extracted(user, reviewDatas);
        return reviewDatas;
    }

    /**
     * 리뷰 모두 조회
     */
    public List<ReviewResponseData> findAll(User user, Boolean sort) {

        Long birth = user.getBirth();
        Gender gender = user.getGender();
        List<Review> reviews;

        List<UserOtt> userOtt = user.getUserOtt();
        List<Ott> otts = new ArrayList<>();

        for (UserOtt ott : userOtt) {
            otts.add(ott.getOtt());
        }

        if(sort == true) {
            reviews = reviewRepository.findAllByOttsOttInAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(otts, birth, gender);
        } else{
            reviews = reviewRepository.findAllByOttsOttInAndDeletedIsFalseOrderByIdDesc(otts);
        }

        List<ReviewResponseData> reviewResponseData = reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

        extracted(user, reviewResponseData);
        return reviewResponseData;
    }

    /**
     * 팔로잉 확인 여부 및 로그인 유저와 리뷰 유저가 같은지 확인
     * @param user
     * @param reviewResponses
     */
    private void extracted(User user, List<ReviewResponseData> reviewResponses) {
        for (ReviewResponseData reviewResponse : reviewResponses) {
            // 만약 유저 아이디와 review 사용자의 아이디가 같다면 loginUser : true
            if(user.getId() == reviewResponse.getUserProfileData().getId()) {
                reviewResponse.setLoginUser(true);
            }
            // 만약 유저 아이디가 review의 아이디를 팔로우 상태라면 followState : true
            if(followRepository.existsByFromUserIdAndToUserId(user.getId(), reviewResponse.getUserProfileData().getId())) {
                reviewResponse.setFollowState(true);
            }

        }
    }
}
