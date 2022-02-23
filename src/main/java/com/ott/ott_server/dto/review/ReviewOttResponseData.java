package com.ott.ott_server.dto.review;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReviewOttResponseData {

    @ApiModelProperty(value = "리뷰의 선택한 OTT 명", example = "WAVVE")
    private String ottName;

}
