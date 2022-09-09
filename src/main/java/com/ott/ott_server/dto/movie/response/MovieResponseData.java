package com.ott.ott_server.dto.movie.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ApiModel(value="영화 조회 결과 정보")
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponseData {

    @ApiModelProperty(value = "제목", example = "셜록")
    private String title;

    private List<MovieOttResponseData> movieOtts;

    @ApiModelProperty(value = "영화 이미지", example = "image.com")
    private String imageUrl;

    @ApiModelProperty(value = "장르", example = "drama")
    private String genre;

    @ApiModelProperty(value = "감독", example = "반구, 반구")
    private String director;

    @ApiModelProperty(value = "배우", example = "반구, 반구")
    private String actor;

    public void addMovieOtts(MovieOttResponseData m) {
        movieOtts.add(m);
    }
}
