package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.*;
import com.ott.ott_server.dto.review.ReviewModificationData;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import com.ott.ott_server.errors.ReviewNotFoundException;
import com.ott.ott_server.errors.UserNotMatchException;
import com.ott.ott_server.infra.GenreRepository;
import com.ott.ott_server.infra.OttRepository;
import com.ott.ott_server.infra.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OttRepository ottRepository;
    private final GenreRepository genreRepository;
    private final Mapper mapper;

    public Review createReview(Movie movie, User user, ReviewRequestData reviewRequestData) {

        Review review = reviewRepository.save(
                Review.builder()
                        .user(user)
                        .attention(reviewRequestData.getAttention())
                        .content(reviewRequestData.getContent())
                        .dialogue(reviewRequestData.getDialogue())
                        .movie(movie)
                        .build()
        );
        return review;
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new ReviewNotFoundException(id));
    }


    public List<ReviewResponseData> getReviews(String ott, String genre) {
        List<Review> reviews = reviewRepository.findByMovieGenreNameAndMovieOttsOttName(genre, ott);

        return reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());
    }

    public Review update(Long reviewId, User user, ReviewModificationData reviewModificationData) {
        Review review = getReviewById(reviewId);
        checkMatchUser(review, user.getId());
        review.update(reviewModificationData);
        return review;
    }

    public void deleteReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
        checkMatchUser(review, user.getId());
        review.setDeleted(true);
    }

    private void checkMatchUser(Review review, Long loginUserId) {
        if(review.getUser().getId() != loginUserId){
            throw new UserNotMatchException();
        }
    }

}
