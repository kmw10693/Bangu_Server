package com.ott.ott_server.controllers;

import com.ott.ott_server.application.MovieService;
import com.ott.ott_server.application.ReviewService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.review.ReviewModificationData;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import com.ott.ott_server.dto.review.response.ReviewSearchData;
import com.ott.ott_server.utils.UserUtil;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "리뷰 API")
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;
    private final UserUtil userUtil;

    /**
     * 리뷰 등록
     * @return
     */
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @PostMapping
    @ApiOperation(value = "리뷰 등록", notes = "전달된 정보에 따라 리뷰를 등록합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    public ReviewResponseData create(@Valid @RequestBody ReviewRequestData reviewRequestData) {
        User user = userUtil.findCurrentUser();
        Movie movie = movieService.getMovieDetailById(reviewRequestData.getMovieId());
        Review review = reviewService.createReview(movie, user, reviewRequestData);
        return review.toReviewResponseData();
    }

    /**
     * 리뷰 상세 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "리뷰 상세 조회", notes = "식별자 값의 리뷰를 상세 조회합니다.")
    @ApiImplicitParam(name = "id", dataType = "integer", value = "리뷰 식별자")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponseData detail(@PathVariable("id") Long id) {
        Review review = reviewService.getReviewById(id);
        return review.toReviewResponseData();
    }

    /**
     * 리뷰를 수정합니다.
     */
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{id}")
    @ApiOperation(value = "리뷰 수정",
            notes = "식별자 값의 리뷰를 수정합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ApiImplicitParam(name = "id", dataType = "integer", value = "리뷰 식별자")
    public ReviewResponseData update(@PathVariable("id") Long id,
                                     @Valid @RequestBody ReviewModificationData reviewModificationData) {
        User user = userUtil.findCurrentUser();
        return reviewService.update(id, user, reviewModificationData).toReviewResponseData();
    }

    /**
     * 리뷰 삭제
     *
     * @param id
     */
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{id}")
    @ApiOperation(value = "리뷰 삭제",
            notes = "식별자 값의 리뷰를 삭제합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ApiImplicitParam(name = "id", dataType = "integer", value = "리뷰 식별자")
    public void destroy(@PathVariable("id") Long id) {
        User user = userUtil.findCurrentUser();
        reviewService.deleteReview(id, user);
    }

    /**
     * ott, 장르 별 리뷰 조회 & 제목
     *
     * @param ott
     * @param genre
     */
    @GetMapping("/{ott}/{genre}")
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "영화 이름으로 리뷰 조회", notes = "ott와 장르, 제목으로 리뷰를 검색 합니다.")
    public ReviewSearchData list(@PathVariable("ott")
                                 @ApiParam(value = "ott 이름 ex) netflix") String ott,
                                 @PathVariable("genre")
                                 @ApiParam(value = "genre 이름 ex) drama") String genre,
                                 @RequestParam @ApiParam(value = "검색 범위(홈: home, 내 방구석: mypage, 피드: feed)", example = "home") String type,
                                 @RequestParam @ApiParam(value = "영화 제목", example = "비밀의 숲") String title,
                                 @RequestParam @ApiParam(value = "성별/나이대 정렬 여부(홈에서만 가능)", example = "false") boolean sortType, Pageable pageable) {
        return reviewService.getReviews(ott, genre, type, title, sortType, pageable);
    }

    /**
     * 제목으로 리뷰 검색 API
     */
    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "영화 이름으로 리뷰 검색", notes = "영화 이름에 해당하는 리뷰를 검색합니다.")
    public List<ReviewResponseData> findListByTitle(@RequestParam @ApiParam(value = "영화 제목") String title,
                                                    @RequestParam(value = "sortType") @ApiParam(value = "성별/나이대 정렬 여부", example = "true") Boolean sortType) {
        User user = userUtil.findCurrentUser();
        return reviewService.findListByTitle(user, title, sortType);
    }

    /**
     * 유저가 구독한 ott 기준으로, 리뷰 전체 가져오기
     */
    @GetMapping("/lists")
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "리뷰 전체 조회", notes = "리뷰를 인자로 받은 값(홈:home, 내 방구석: myReviews, 피드: following)에 따라 정렬합니다.")
    public Page<ReviewResponseData> findAll(@RequestParam @ApiParam(value = "표시 기준", example = "home") String type, Pageable pageable) {
        User user = userUtil.findCurrentUser();
        return reviewService.findAll(user, type, pageable);
    }

    /**
     * 해당 유저의 리뷰 가져오기
     */
    @GetMapping("/lists/{userId}")
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN", value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저의 리뷰 조회", notes = "유저 인덱스를 받아 리뷰를 조회합니다.")
    public Page<ReviewResponseData> findAll(@PathVariable @ApiParam(value = "유저 인덱스", example = "1") Long userId, Pageable pageable) {
        User user = userUtil.findCurrentUser();
        return reviewService.findAllByUser(user, userId, pageable);
    }

    @GetMapping("/bookmark")
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN", value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "유저의 북마크한 리뷰 조회", notes = "유저의 북마크한 리뷰를 가져옵니다.")
    public Page<ReviewResponseData> getBookmark(Pageable pageable) {
        User user = userUtil.findCurrentUser();
        return reviewService.getBookmark(pageable);
    }

    @PostMapping("/bookmark/{reviewId}")
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN", value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "리뷰 북마크 및 북마크 해제 하기", notes = "리뷰를 북마크시 true, 북마크 해제시 false를 반환 합니다.")
    @ApiResponse(responseCode = "200", description = "북마크 및 북마크 해제를 성공적으로 한 경우")
    public ResponseEntity<Boolean> bookmark(@PathVariable @ApiParam(value = "리뷰 인덱스", example = "1") Long reviewId){
            return ResponseEntity.ok(reviewService.bookmark(reviewId));
    }

}
