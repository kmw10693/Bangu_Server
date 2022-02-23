package com.ott.ott_server.dto.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel
public class MovieOttResponseData {

    @ApiModelProperty(value = "구독 ott 식별자", example = "1")
    private Long ottId;

    @ApiModelProperty(value = "영화가 소속하는 OTT 명", example = "NETFLIX")
    private String ottName;
}
