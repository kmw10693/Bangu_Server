package com.ott.ott_server.dto.review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(value = "리뷰가 선택한 ott 반환 정보")
public class ReviewOttResponseData {

    @ApiModelProperty(value = "리뷰의 ott 인덱스", example = "1")
    private Long id;

    @ApiModelProperty(value = "리뷰의 선택한 OTT 명", example = "WAVVE")
    private String ottName;

}
