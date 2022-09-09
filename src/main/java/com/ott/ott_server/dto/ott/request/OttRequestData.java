package com.ott.ott_server.dto.ott.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@ApiModel(description = "OTT 목록 저장 요청 데이터 모델")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OttRequestData {

    @ApiModelProperty(value = "OTT 이름", example = "NETFLIX", required = true)
    private String name;

}
