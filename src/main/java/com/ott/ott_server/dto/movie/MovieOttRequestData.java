package com.ott.ott_server.dto.movie;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MovieOttRequestData {

    @ApiParam(value = "tving 선택 여부", required = true, example = "true")
    private boolean tving;

    @ApiParam(value = "watcha 선택 여부", required = true, example = "true")
    private boolean watcha;

    @ApiParam(value = "netflix 선택 여부", required = true, example = "true")
    private boolean netflix;

    @ApiParam(value = "wavve 선택 여부", required = true, example = "true")
    private boolean wavve;

    public MovieOttRequestData() {
        tving = false;
        watcha = false;
        netflix = false;
        wavve = false;
    }
}
