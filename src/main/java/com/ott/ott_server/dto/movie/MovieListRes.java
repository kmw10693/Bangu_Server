package com.ott.ott_server.dto.movie;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.OptionalDouble;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "회원가입 축하 영화 결과 정보")
public class MovieListRes {

    @ApiModelProperty(value = "영화 식별자", example = "1")
    private Long movieId;

    @ApiModelProperty(value = "영화 이미지", example = "image.com")
    private String imageUrl;

    @ApiModelProperty(value = "영화 평점", example = "4.5")
    private double score;

}
