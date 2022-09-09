package com.ott.ott_server.dto.movie.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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

    @NotBlank
    @ApiModelProperty(value = "영화 장르", example = "#드라마 #스릴러")
    private String genre;

    @ApiModelProperty(value = "영화 이미지", example = "https://image.tving.com/upload/cms/caip/CAIP0900/P000380913.jpg")
    private String imageUrl;

    @ApiModelProperty(value = "제작년도", example = "2017")
    private String birth;

    @NotBlank
    @ApiModelProperty(value = "감독", example = "안길호")
    private String director;

    @NotBlank
    @ApiModelProperty(value = "등장인물", example = "조승우, 배두나, 유재명, 이준혁, 신혜선, 이경영, 윤세아, 예수정, 이태형, 전배수")
    private String actor;

    private MovieOttRequestData movieOttRequestData;

}
