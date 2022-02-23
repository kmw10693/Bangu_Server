package com.ott.ott_server.controllers;

import com.ott.ott_server.application.MovieService;
import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.dto.movie.MovieRequestData;
import com.ott.ott_server.dto.movie.MovieResponseData;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @ApiOperation(value = "영화 생성", notes = "영화를 생성합니다.")
    @ResponseStatus(HttpStatus.CREATED)
    public MovieResponseData create(@Valid @RequestBody MovieRequestData movieRequestData) {
        Movie movie = movieService.registerMovie(movieRequestData);
        return movie.toMovieResponseData();

    }

    @GetMapping
    @ApiOperation(value = "영화 리스트 조회", notes = "영화 전체 리스트를 정렬하여 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    public List<MovieResponseData> list() {
        return movieService.getMovies();
    }

    /**
     * 입력한 식별자 값(id)에 해당하는 영화의 상세 정보를 가져옵니다.
     * [GET] /books/{id}
     * @return Book
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "영화 상세 조회", notes = "식별자 값의 영화를 상세 조회합니다.")
    @ResponseStatus(HttpStatus.OK)
    public MovieResponseData detail(@PathVariable("id") @ApiParam(value = "영화 식별자 값") Long id) {
        Movie movie = movieService.getMovieDetailById(id);
        return movie.toMovieResponseData();

    }

    /**
     * 해당하는 제목의 영화를 가져옵니다.
     * [GET] /books/search?name=
     * @return Book
     */
    @GetMapping("/search")
    @ApiOperation(value = "영화 검색", notes = "영화 이름에 검색어가 포함 된 영화 리스트를 가져옵니다.")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<MovieResponseData> search(@RequestParam(name = "name") @ApiParam(value = "영화 이름 검색어") String search) {
        return movieService.getSearchMovies(search);
    }


}
