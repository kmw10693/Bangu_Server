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
    public ReviewResponseData detail(@PathVariable("id") Long id) {
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
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
