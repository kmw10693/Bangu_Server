package com.ott.ott_server.controllers;

import com.ott.ott_server.application.CrawService;
import com.ott.ott_server.application.MovieService;
import com.ott.ott_server.domain.Movie;
import com.ott.ott_server.dto.movie.request.MovieOttRequestData;
import com.ott.ott_server.dto.movie.response.MovieListResponseData;
import com.ott.ott_server.dto.movie.request.MovieRequestData;
import com.ott.ott_server.dto.movie.response.MovieResponseData;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

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

    @GetMapping("/lists")
    @ApiOperation(value = "회원가입 성공 시 영화 리스트 보기", notes = "영화 리스트의 평점과 표지 이미지를 페이징 처리하여 가져옵니다.")
    public Page<MovieListResponseData> getMovies(@PageableDefault(size = 10) Pageable pageable) {
        return movieService.getMovieLists(pageable);
    }

    @GetMapping("/search")
    @ApiOperation(value = "영화 이름으로 검색", notes = "영화 이름에 검색어가 포함된 영화 리스트를 크롤링하여 가져옵니다.")
    public Page<MovieResponseData> search(@RequestParam @ApiParam(value = "영화 이름") String name, Pageable pageable) throws IOException {
        return crawService.getSearch(name, pageable);
    }

}
