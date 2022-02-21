package com.ott.ott_server.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@ApiModel("사용자 구독 정보")
public class UserOttResponseData {

    @ApiModelProperty(value = "유저 식별자", example = "1")
    private Long userId;

    @ApiModelProperty(value = "구독 ott 식별자", example = "1")
    private Long ottId;

    @ApiModelProperty(value = "구독 ott 이름", example = "netflix")
    private String ottName;

}
