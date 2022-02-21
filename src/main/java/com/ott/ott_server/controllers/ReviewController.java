package com.ott.ott_server.controllers;

import com.ott.ott_server.application.MovieService;
import com.ott.ott_server.application.ReviewService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.Review;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import com.ott.ott_server.dto.user.UserResultData;
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
    @ApiOperation(value = "리뷰 등록", notes = "전달된 정보에 따라 리뷰를 등록합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewResponseData create(@Valid @RequestBody ReviewRequestData reviewRequestData,
                                     Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        Review review = reviewService.createReview(user, reviewRequestData);
        return review.toReviewResponseData();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "리뷰 상세 조회", notes = "식별자 값의 리뷰를 상세 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    public ReviewResponseData detail(@PathVariable("id") @ApiParam(value = "리뷰 식별자 값") Long id) {
        Review review = reviewService.getReviewById(id);
        return review.toReviewResponseData();
    }

    @GetMapping("/{ott}/{genre}")
    @ApiOperation(value = "OTT/장르 별 리뷰 전체 조회", notes = "OTT/장르 별 리뷰를 전체 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    public List<ReviewResponseData> read(@PathVariable("ott") @ApiParam(value = "조회 하려는 ott") String ott,
                                         @PathVariable("genre") @ApiParam(value = "조회 하려는 장르") String genre) {
        return reviewService.getReviewsByOttAndGenre(ott, genre);
    }

}
