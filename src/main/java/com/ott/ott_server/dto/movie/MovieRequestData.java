package com.ott.ott_server.dto.movie;

import com.ott.ott_server.domain.Movie;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value="영화 등록 요청 정보")
public class MovieRequestData {

    @NotBlank
    @ApiModelProperty(value = "제목", example = "비밀의 숲")
    private String title;

    private MovieOttRequestData movieOttRequestData;

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

}
