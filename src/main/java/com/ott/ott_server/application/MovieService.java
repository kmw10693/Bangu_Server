package com.ott.ott_server.application;

import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.domain.MovieOtt;
import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.dto.movie.MovieListRes;
import com.ott.ott_server.dto.movie.MovieRequestData;
import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.dto.movie.response.Item;
import com.ott.ott_server.dto.movie.response.NaverResponseData;
import com.ott.ott_server.errors.GenreNotFoundException;
import com.ott.ott_server.errors.MovieNotFoundException;
import com.ott.ott_server.infra.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final RestTemplate restTemplate;
    private final MovieRepository movieRepository;
    private final OttRepository ottRepository;
    private final MovieOttRepository movieOttRepository;

    @Value("${social.naver.client-id}")
    private String CLIENT_ID;
    @Value("${social.naver.client-secret}")
    private String CLIENT_SECRET;


    public List<MovieResponseData> getMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieResponseData> movieResponseData = movies
                .stream()
                .map(Movie::toMovieResponseData)
                .collect(Collectors.toList());

        return movieResponseData;
    }

    public Movie getMovieDetailById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
    }

    public Page<MovieResponseData> getSearchMovies(String search, Pageable pageable) throws ParseException {

        String apiURL = "https://openapi.naver.com/v1/search/movie?query={search}";
        final HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", CLIENT_ID);
        headers.set("X-Naver-Client-Secret", CLIENT_SECRET);

        final HttpEntity<String> entity = new HttpEntity<>(headers);

        NaverResponseData searchMovies = restTemplate.exchange(apiURL, HttpMethod.GET, entity, NaverResponseData.class, search).getBody();

        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < searchMovies.getDisplay(); i++) {
            Item item = searchMovies.getItems().get(i);
            Movie movie = movieRepository.findByTitleContainingAndDirector(item.getTitle(), item.getDirector());

            if (movie == null) {
                movie = movieRepository.save(Movie.builder()
                        .title(item.getTitle())
                        .imageUrl(item.getImage())
                        .director(item.getDirector())
                        .actor(item.getActor())
                        .genre("#영화")
                        .build());
            }
            movies.add(movie);
        }

        List<MovieResponseData> movieResponseData = movies.stream()
                .map(Movie::toMovieResponseData)
                .collect(Collectors.toList());

        return new PageImpl<>(movieResponseData, pageable, movies.size());
    }

    public Movie registerMovie(MovieRequestData movieRequestData) {

        Movie movie = movieRepository.save(
                Movie.builder()
                        .actor(movieRequestData.getActor())
                        .director(movieRequestData.getDirector())
                        .imageUrl(movieRequestData.getImageUrl())
                        .title(movieRequestData.getTitle())
                        .genre(movieRequestData.getGenre())
                        .build()
        );
        checkSubscribe(movieRequestData, movie);
        return movie;
    }

    private void checkSubscribe(MovieRequestData movieRequestData, Movie movie) {
        if (movieRequestData.getMovieOttRequestData().isNetflix()) {
            Optional<Ott> ott = findIdByOttName("NETFLIX");
            setMovieOtt(movie, ott);
        }
        if (movieRequestData.getMovieOttRequestData().isTving()) {
            Optional<Ott> ott = findIdByOttName("TVING");
            setMovieOtt(movie, ott);
        }
        if (movieRequestData.getMovieOttRequestData().isWatcha()) {
            Optional<Ott> ott = findIdByOttName("WATCHAPLAY");
            setMovieOtt(movie, ott);
        }
        if (movieRequestData.getMovieOttRequestData().isWavve()) {
            Optional<Ott> ott = findIdByOttName("WAVVE");
            setMovieOtt(movie, ott);
        }

    }

    private Optional<Ott> findIdByOttName(String title) {
        return ottRepository.findByName(title);
    }

    private void setMovieOtt(Movie movie, Optional<Ott> ott) {
        movieOttRepository.save(MovieOtt.builder()
                .movie(movie)
                .ott(ott.get())
                .build()
        );
    }

    public Page<MovieListRes> getMovieLists(Pageable pageable) {
        Page<Movie> movies = movieRepository.findAllByDeletedFalse(pageable);

        List<MovieListRes> movieListRes = movies
                .stream()
                .map(r -> r.toMovieListRes(r.getReviews()))
                .collect(Collectors.toList());

        return new PageImpl<>(movieListRes, pageable, movies.getTotalElements());
    }

}
