package com.ott.ott_server.controllers;

import com.ott.ott_server.application.CrawService;
import com.ott.ott_server.application.MovieService;
import com.ott.ott_server.application.ReviewService;
import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.dto.movie.MovieListRes;
import com.ott.ott_server.dto.movie.MovieRequestData;
import com.ott.ott_server.dto.movie.MovieResponseData;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.json.simple.parser.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Api(tags = "영화 API")
@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final CrawService crawService;

    @PostMapping
    @ApiOperation(value = "영화 생성", notes = "주어진 정보를 받아 영화를 생성합니다.",
            response = MovieResponseData.class)
    public MovieResponseData create(@Valid @RequestBody MovieRequestData movieRequestData) {
        Movie movie = movieService.registerMovie(movieRequestData);
        return movie.toMovieResponseData();

    }

    /**
     * 영화 리스트와 평점을 pageable 기준에 따라 조회합니다.
     * [GET] /movies/lists?page= &size= &sort= , 정렬방식
     */
    @GetMapping("/lists")
    @ApiOperation(value = "회원가입 성공시 영화 리스트", notes = "영화 리스트의 평점과 표지 이미지를 가져옵니다.")
    public Page<MovieListRes> getMovies(Pageable pageable) {
        return movieService.getMovieLists(pageable);
    }

    @GetMapping("/search")
    @ApiOperation(value = "영화 이름으로 검색", notes = "영화 이름에 검색어가 포함된 영화 리스트를 가져오고 DB에 저장합니다.")
    public Page<MovieResponseData> search(@RequestParam @ApiParam(value = "영화 이름") String name, Pageable pageable) throws IOException {
        return crawService.getSearch(name, pageable);
    }

}
