package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.*;
import com.ott.ott_server.dto.review.ReviewRequestData;
import com.ott.ott_server.dto.review.ReviewResponseData;
import com.ott.ott_server.errors.ReviewNotFoundException;
import com.ott.ott_server.infra.GenreRepository;
import com.ott.ott_server.infra.OttRepository;
import com.ott.ott_server.infra.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OttRepository ottRepository;
    private final GenreRepository genreRepository;
    private final Mapper mapper;

    public Review createReview(User user, ReviewRequestData reviewRequestData) {

        Movie movie = mapper.map(reviewRequestData.getMovieResponseData(), Movie.class);
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

    public List<ReviewResponseData> getReviewsByOttAndGenre(String ottName, String genreName) {
        Optional<Ott> ott = ottRepository.findByNameContaining(ottName);
        Optional<Genre> genre = genreRepository.findByNameContaining(genreName);

        Long ottId = ott.get().getId();
        Long genreId = genre.get().getId();

        List<Review> reviews = reviewRepository.findByMovie_GenreIdAndMovie_OttsId(genreId, ottId);
        return reviews.stream()
                .map(Review::toReviewResponseData)
                .collect(Collectors.toList());

    }

}
