package com.ott.ott_server.dto.review;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("영화 장르")
public class GenreRequestData {
    @ApiModelProperty(value = "영화 장르", example = "#드라마")
    private List<String> genre;

}
