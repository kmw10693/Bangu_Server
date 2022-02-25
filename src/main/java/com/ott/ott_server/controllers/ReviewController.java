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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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


    @GetMapping("/{id}")
    @ApiOperation(value = "리뷰 상세 조회",
            notes = "식별자 값의 리뷰를 상세 조회합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponseData detail(@PathVariable("id") @ApiParam(value = "리뷰 식별자 값") Long id) {
        Review review = reviewService.getReviewById(id);
        return review.toReviewResponseData();
    }

    /**
     * 리뷰를 수정합니다.
     */
    @PatchMapping("/{id}")
    @ApiOperation(value = "리뷰 수정",
            notes = "식별자 값의 리뷰를 수정합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    public ReviewResponseData update(@PathVariable("id") @ApiParam(value = "리뷰 식별자 값") Long id,
                                     @Valid @RequestBody ReviewModificationData reviewModificationData,
                                     Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        return reviewService.update(id, user, reviewModificationData).toReviewResponseData();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "리뷰 삭제",
            notes = "식별자 값의 리뷰를 삭제합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ResponseStatus(HttpStatus.OK)
    public void destroy(@PathVariable("id") @ApiParam(value = "리뷰 식별자 값") Long id,
                        Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        reviewService.deleteReview(id, user);
    }

    @GetMapping("/{ott}/{genre}")
    @ApiOperation(value = "ott별, 장르 별 리뷰 상세 조회",
            notes = "ott와 장르 별 리뷰를 상세 조회합니다. 헤더에 사용자 토큰 주입을 필요로 합니다.")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseData> list(@PathVariable("ott")
                                   @ApiParam(value = "ott 이름 ex) netflix") String ott,
                                   @PathVariable("genre")
                                   @ApiParam(value = "genre 이름 ex) drama") String genre) {

        return reviewService.getReviews(ott, genre);
    }

}
