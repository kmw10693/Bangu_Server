package com.ott.ott_server.dto.review;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@ApiModel(value="리뷰 ott 등록 요청 정보")
public class ReviewOttRequestData {

    @ApiParam(value = "tving 선택 여부", required = true, example = "true")
    private boolean tving;

    @ApiParam(value = "watcha 선택 여부", required = true, example = "true")
    private boolean watcha;

    @ApiParam(value = "netflix 선택 여부", required = true, example = "true")
    private boolean netflix;

    @ApiParam(value = "wavve 선택 여부", required = true, example = "true")
    private boolean wavve;

    public ReviewOttRequestData() {
        tving = false;
        watcha = false;
        netflix = false;
        wavve = false;
    }
}
