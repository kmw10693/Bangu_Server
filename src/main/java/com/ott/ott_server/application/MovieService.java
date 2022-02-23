package com.ott.ott_server.application;

import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.domain.MovieOtt;
import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.dto.movie.MovieOttRequestData;
import com.ott.ott_server.dto.movie.MovieOttResponseData;
import com.ott.ott_server.dto.movie.MovieRequestData;
import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.errors.MovieNotFoundException;
import com.ott.ott_server.infra.MovieOttRepository;
import com.ott.ott_server.infra.MovieRepository;
import com.ott.ott_server.infra.OttRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final OttRepository ottRepository;
    private final MovieOttRepository movieOttRepository;

    public List<MovieResponseData> getMovies() {
        List<Movie> movies = movieRepository.findAll();
        List<MovieResponseData> movieResponseData = movies
                .stream()
                .map(Movie::toMovieResponseData)
                .collect(Collectors.toList());

        return movieResponseData;
    }

    public Movie getMovieDetailById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException(id));
        return movie;
    }

    public List<MovieResponseData> getSearchMovies(String search) {

        List<Movie> movies = movieRepository.findAllByTitleContaining(search);
        return movies.stream()
                .map(Movie::toMovieResponseData)
                .collect(Collectors.toList());
    }

    public Movie registerMovie(MovieRequestData movieRequestData) {
        Movie movie = movieRepository.save(
                Movie.builder()
                        .actor(movieRequestData.getActor())
                        .director(movieRequestData.getDirector())
                        .birth(movieRequestData.getBirth())
                        .imageUrl(movieRequestData.getImageUrl())
                        .title(movieRequestData.getTitle())
                        .build()
        );
        checkSubscribe(movieRequestData.getMovieOttRequestData(), movie);
        return movie;
    }

    private void checkSubscribe(MovieOttRequestData movieOttRequestData, Movie movie) {
        if (movieOttRequestData.isNetflix()) {
            Optional<Ott> ott = findIdByOttName("netflix");
            setMovieOtt(movie, ott);
        }
        if (movieOttRequestData.isTving()){
            Optional<Ott> ott = findIdByOttName("tving");
            setMovieOtt(movie, ott);
        }
        if(movieOttRequestData.isWatcha()) {
            Optional<Ott> ott = findIdByOttName("watcha");
            setMovieOtt(movie, ott);
        }
        if(movieOttRequestData.isWavve()) {
            Optional<Ott> ott = findIdByOttName("wavve");
            setMovieOtt(movie, ott);
        }

    }

    private Optional<Ott> findIdByOttName(String title) {
        return ottRepository.findByNameContaining(title);
    }

    private void setMovieOtt(Movie movie, Optional<Ott> ott) {
        movieOttRepository.save(MovieOtt.builder()
                .movie(movie)
                .ott(ott.get())
                .build()
        );
    }

}
