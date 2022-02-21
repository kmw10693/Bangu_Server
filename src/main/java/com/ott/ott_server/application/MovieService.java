package com.ott.ott_server.application;

import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.errors.MovieNotFoundException;
import com.ott.ott_server.infra.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public List<MovieResponseData> getMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieResponseData> movieResponseData = movies
                .stream()
                .map(Movie::toMovieResponseData)
                .collect(Collectors.toList());

        return movieResponseData;
    }

    public MovieResponseData getMovieDetailById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        return movie.toMovieResponseData();
    }

    public List<MovieResponseData> getSearchMovies(String search) {

        List<Movie> movies = movieRepository.findAllByTitleContaining(search);
        return movies.stream()
                .map(Movie::toMovieResponseData)
                .collect(Collectors.toList());
    }

}
