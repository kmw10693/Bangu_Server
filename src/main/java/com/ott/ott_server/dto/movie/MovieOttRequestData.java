package com.ott.ott_server.dto.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@ApiModel(value="영화 ott 등록 요청 정보")
public class MovieOttRequestData {

    @ApiModelProperty(value = "tving 소속 여부", example = "true")
    private boolean tving;

    @ApiModelProperty(value = "watcha 소속 여부", example = "true")
    private boolean watcha;

    @ApiModelProperty(value = "netflix 소속 여부", example = "true")
    private boolean netflix;

    @ApiModelProperty(value = "wavve 소속 여부", example = "true")
    private boolean wavve;

    public MovieOttRequestData() {
        tving = false;
        watcha = false;
        netflix = false;
        wavve = false;
    }
}
