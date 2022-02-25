package com.ott.ott_server.dto.movie;

import com.ott.ott_server.domain.Movie;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@Builder
@ApiModel(value="영화 등록 요청 정보")
public class MovieRequestData {

    @NotBlank
    @ApiModelProperty(value = "제목", example = "비밀의 숲")
    private String title;

    @ApiParam(value = "tving 선택 여부", required = true, example = "true")
    private boolean tving;

    @ApiParam(value = "watcha 선택 여부", required = true, example = "true")
    private boolean watcha;

    @ApiParam(value = "netflix 선택 여부", required = true, example = "true")
    private boolean netflix;

    @ApiParam(value = "wavve 선택 여부", required = true, example = "true")
    private boolean wavve;

    @NotBlank
    @ApiParam(value = "장르", required = true, example = "drama")
    private String genre;

    @ApiModelProperty(value = "영화 이미지", example = "image.com")
    private String imageUrl;

    @ApiModelProperty(value = "제작년도", example = "2017")
    private String birth;

    @NotBlank
    @ApiModelProperty(value = "감독", example = "봉준호")
    private String director;

    @NotBlank
    @ApiModelProperty(value = "등장인물", example = "배두나, 조승우")
    private String actor;

    public MovieRequestData() {
        tving = false;
        watcha = false;
        netflix = false;
        wavve = false;
    }

}
