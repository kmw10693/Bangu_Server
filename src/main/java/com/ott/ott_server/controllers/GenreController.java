package com.ott.ott_server.controllers;

import com.ott.ott_server.application.GenreService;
import com.ott.ott_server.domain.Genre;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @PostMapping
    @ApiOperation(value = "장르 저장", notes = "장르 정보를 받아 장르를 저장합니다.",
            response = Genre.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Genre save(@RequestBody Genre genre) {
        return genreService.save(genre);
    }
}
