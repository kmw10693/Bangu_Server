package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.*;
import com.ott.ott_server.domain.enums.Gender;
import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.dto.review.ReviewModificationData;
import com.ott.ott_server.dto.review.ReviewOttResponseData;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import com.ott.ott_server.dto.review.response.ReviewRes;
import com.ott.ott_server.dto.review.response.ReviewSearchData;
import com.ott.ott_server.errors.OttNameNotFoundException;
import com.ott.ott_server.errors.ReviewNotFoundException;
import com.ott.ott_server.errors.UserNotMatchException;
import com.ott.ott_server.infra.*;
import com.ott.ott_server.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final UserUtil userUtil;
    private final ReviewRepository reviewRepository;
    private final FollowRepository followRepository;
    private final OttRepository ottRepository;
    private final ReviewOttRepository reviewOttRepository;
    private final MovieRepository movieRepository;
    private final BookMarkRepository bookMarkRepository;

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
        if (reviewRequestData.getReviewOtt().isTving()) {
            Ott ott = findIdByOttName("tving");
            setReviewOtt(review, ott);
        }
        if (reviewRequestData.getReviewOtt().isWatcha()) {
            Ott ott = findIdByOttName("watcha");
            setReviewOtt(review, ott);
        }
        if (reviewRequestData.getReviewOtt().isWavve()) {
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
     *
     * @param ott
     * @param genre
     */
    public ReviewSearchData getReviews(String ott, String genre, String type, String title, boolean sortType, Pageable pageable) {

        User user = userUtil.findCurrentUser();
        Long birth = user.getBirth();
        Gender gender = user.getGender();
        Page<Review> reviews = null;

        if (type.equals("home")) {
            if (sortType == false) {
                reviews = reviewRepository.findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(genre, ott, title, pageable);
            } else {
                reviews = reviewRepository.findByMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndUserBirthAndUserGenderAndDeletedIsFalseOrderByIdDesc(genre, ott, title, birth, gender, pageable);
            }
        } else if (type.equals("mypage")) {
            reviews = reviewRepository.findByUserAndMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(user, genre, ott, title, pageable);
        } else if (type.equals("feed")) {
            List<Follow> followList = followRepository.findByFromUser(user);
            List<User> followings = new ArrayList<>();
            followList.stream().forEach(r -> followings.add(r.getToUser()));
            reviews = reviewRepository.findByUserInAndMovieGenreNameAndMovieOttsOttNameAndMovieTitleContainingAndDeletedIsFalseOrderByIdDesc(followings, genre, ott, title, pageable);
        }

        List<ReviewRes> reviewRes = new ArrayList<>();
        List<Movie> movie = movieRepository.findByGenreNameAndOttsOttNameAndTitleContainingAndDeletedIsFalse(genre, ott, title);

        List<MovieResponseData> movieResponseData = movie.stream()
                .map(Movie::toMovieResponseData).collect(Collectors.toList());

        for (Review review : reviews) {
            Boolean followState = followRepository.existsByFromUserIdAndToUserId(user.getId(), review.getUser().getId());
            Boolean bookmark = bookMarkRepository.existsByReviewAndUser(review, user);

            if (user.getId() == review.getUser().getId()) {
                followState = null;
                bookmark = null;
            }
            reviewRes.add(ReviewRes.builder()
                    .id(review.getId())
                    .userProfileData(review.getUser().toUserProfileData())
                    .followState(followState)
                    .bookMark(bookmark)
                    .content(review.getContent())
                    .title(review.getMovie().getTitle())
                    .genre(review.getMovie().getGenre().getName())
                    .imageUrl(review.getMovie().getImageUrl())
                    .score(review.getScore())
                    .reviewOtts(ReviewOttResponseData.of(review.getOtts()))
                    .build());
        }

        return new ReviewSearchData(movieResponseData, new PageImpl<>(reviewRes, pageable, reviewRes.size()));
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
    public List<ReviewResponseData> findListByTitle(User user, String title, Boolean sort) {

        Long birth = user.getBirth();
        Gender gender = user.getGender();
        List<Review> reviews;

        if (sort == true) {
            reviews = reviewRepository.findByMovieTitleContainingAndUserBirthAndUserGenderAndDeletedFalseOrderByIdDesc(title, birth, gender);
        } else {
            reviews = reviewRepository.findByMovieTitleContainingAndDeletedFalseOrderByIdDesc(title);
        }
        List<ReviewResponseData> reviewDatas = reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

        checkFollowing(user, reviewDatas);
        return reviewDatas;
    }

    /**
     * 리뷰 모두 조회
     */
    public Page<ReviewResponseData> findAll(User user, String type, Pageable pageable) {

        Long birth = user.getBirth();
        Gender gender = user.getGender();
        Page<Review> reviews = null;

        List<UserOtt> userOtt = user.getUserOtt();
        List<Ott> otts = new ArrayList<>();

        for (UserOtt ott : userOtt) {
            otts.add(ott.getOtt());
        }

        // 만약 홈인 경우
        if (type.equals("home")) {
            reviews = reviewRepository.findByOttsOttInAndDeletedIsFalseOrderByIdDesc(otts, pageable);
        }
        // 만약 본인인 경우
        else if (type.equals("myReviews")) {
            reviews = reviewRepository.findByUserOrderByIdDesc(user, pageable);
        }
        // 팔로잉하는 경우
        else if (type.equals("following")) {
            // 팔로우 리스트를 가져온다.
            List<Follow> followList = followRepository.findByFromUser(user);
            List<User> followings = new ArrayList<>();
            // 유저 팔로우에 팔로우를 추가한다.
            followList.stream().forEach(r -> followings.add(r.getToUser()));
            // 팔로우 리스트의 리뷰들을 리스트로 가져오고 저장한다.
            reviews = reviewRepository.findByUserInAndDeletedFalseOrderByIdDesc(followings, pageable);
        }

        List<Review> sortReviews = reviews.stream().filter(distinctByKey(r -> r.getId())).collect(Collectors.toList());

        List<ReviewResponseData> reviewResponseData = sortReviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

        checkFollowing(user, reviewResponseData);
        return new PageImpl<>(reviewResponseData, pageable, sortReviews.size());
    }

    /**
     * 해당 유저의 리뷰 조회
     */
    public Page<ReviewResponseData> findAllByUser(User user, Long userId, Pageable pageable) {

        // 유저 아이디의 해당하는 리뷰를 전체 가져오기
        Page<Review> reviews = reviewRepository.findByUserIdAndDeletedFalseOrderByIdDesc(userId, pageable);

        List<ReviewResponseData> reviewResponseData = reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

        checkFollowing(user, reviewResponseData);
        return new PageImpl<>(reviewResponseData, pageable, reviews.getTotalElements());
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> map = new HashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    /**
     * 팔로잉 확인 여부 및 로그인 유저와 리뷰 유저가 같은지 확인
     *
     * @param user
     * @param reviewResponses
     */
    private void checkFollowing(User user, List<ReviewResponseData> reviewResponses) {
        for (ReviewResponseData reviewResponse : reviewResponses) {
            // 만약 유저 아이디와 review 사용자의 아이디가 같다면 loginUser : true
            if (user.getId() == reviewResponse.getUserProfileData().getId()) {
                reviewResponse.setLoginUser(true);
            }
            // 만약 유저 아이디가 review의 아이디를 팔로우 상태라면 followState : true
            if (followRepository.existsByFromUserIdAndToUserId(user.getId(), reviewResponse.getUserProfileData().getId())) {
                reviewResponse.setFollowState(true);
            }

        }
    }

    /**
     * 북마크한 리뷰 가져오기
     */
    public Page<ReviewResponseData> getBookmark(Pageable pageable) {
        User user = userUtil.findCurrentUser();
        List<BookMark> bookMarks = bookMarkRepository.findByUser(user);
        List<ReviewResponseData> reviews = bookMarks.stream().map(r -> r.getReview().toReviewResponseData()).collect(Collectors.toList());
        return new PageImpl<>(reviews, pageable, reviews.size());
    }

}
