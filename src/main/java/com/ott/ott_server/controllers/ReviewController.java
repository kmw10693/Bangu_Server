package com.ott.ott_server.controllers;

import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.MovieService;
import com.ott.ott_server.application.ReviewService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.review.ReviewModificationData;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final MovieService movieService;
    private final FollowService followService;

    /**
     * 리뷰 등록
     * @param reviewRequestData
     * @param authentication
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @PostMapping
    @ApiOperation(value = "리뷰 등록",
            notes = "전달된 정보에 따라 리뷰를 등록합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseData create(@Valid @RequestBody ReviewRequestData reviewRequestData,
                                     Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        Movie movie = movieService.getMovieDetailById(reviewRequestData.getMovieId());
        Review review = reviewService.createReview(movie, user, reviewRequestData);
        return review.toReviewResponseData();
    }

    /**
     * 리뷰 상세 조회
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{id}")
    @ApiOperation(value = "리뷰 상세 조회",
            notes = "식별자 값의 리뷰를 상세 조회합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ApiImplicitParam(name = "id", dataType = "integer", value = "리뷰 식별자")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponseData detail(@PathVariable("id") Long id,
                                     Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        Review review = reviewService.getReviewById(id);
        return review.toReviewResponseData();
    }

    /**
     * 리뷰를 수정합니다.
     */
    @PreAuthorize("hasRole('ROLE_USER')")
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
                                     @Valid @RequestBody ReviewModificationData reviewModificationData,
                                     Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        return reviewService.update(id, user, reviewModificationData).toReviewResponseData();
    }

    /**
     * 리뷰 삭제
     *
     * @param id
     * @param authentication
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{id}")
    @ApiOperation(value = "리뷰 삭제",
            notes = "식별자 값의 리뷰를 삭제합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParam(name = "id", dataType = "integer", value = "리뷰 식별자")
    public void destroy(@PathVariable("id") Long id,
                        Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        reviewService.deleteReview(id, user);
    }

    /**
     * ott, 장르 별 리뷰 조회 & 제목
     * @param ott
     * @param genre
     * @return
     */
    @GetMapping("/{ott}/{genre}")
    @ApiOperation(value = "ott, 장르, 제목, 성별 / 나이대 맞춤으로 리뷰 조회",
            notes = "ott와 장르, 제목으로 리뷰를 검색 합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseData> list(@PathVariable("ott")
                                         @ApiParam(value = "ott 이름 ex) netflix") String ott,
                                         @PathVariable("genre")
                                         @ApiParam(value = "genre 이름 ex) drama") String genre,
                                         @RequestParam(value = "title") @ApiParam(value = "영화 제목", example = "비밀의 숲") String title,
                                         @RequestParam(value = "sort") @ApiParam(value = "성별/나이대 정렬 여부", example = "true") Boolean sort,
                                         Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        return reviewService.getReviews(user, ott, genre, title, sort);
    }

    /**
     * 제목으로 리뷰 검색 API
     */
    @GetMapping
    @ApiOperation(value = "영화 이름으로 리뷰 검색", notes = "영화 이름에 해당하는 리뷰를 검색합니다.")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseData> findListByTitle(@RequestParam @ApiParam(value = "영화 제목") String title,
                                                    @RequestParam(value = "sort") @ApiParam(value = "성별/나이대 정렬 여부", example = "true") Boolean sort,
                                                    Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        return reviewService.findListByTitle(user, title, sort);
    }

    /**
     * 유저가 구독한 ott 기준으로, 리뷰 전체 가져오기
     */
    @GetMapping("/lists")
    @ApiOperation(value = "리뷰를 전체 조회", notes = "리뷰를 전체 조회합니다.")
    public List<ReviewResponseData> findAll(Authentication authentication,
                                            @RequestParam(value = "sort") @ApiParam(value = "성별/나이대 정렬 여부", example = "true") Boolean sort) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        return reviewService.findAll(user, sort);
    }

}
