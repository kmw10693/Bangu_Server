package com.ott.ott_server.dto.review.request;

import com.ott.ott_server.dto.movie.response.MovieOttResponseData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@ApiModel(value="리뷰 등록 요청 정보")
@AllArgsConstructor
public class ReviewRequestData {

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

    private ReviewOttRequestData reviewOtt;

    @ApiModelProperty(value = "별점", required = true, example = "4.5")
    @DecimalMin("0") @DecimalMax("5")
    @NotNull(message = "별점을 입력해주세요.")
    private BigDecimal score;

    @ApiModelProperty(value = "감상 포인트", required = true, example = "좋아요")
    @NotNull(message = "감상 포인트를 입력해주세요.")
    private String attention;

    @ApiModelProperty(value = "인상깊은 대사", required = true, example = "기억이 안난다.")
    @NotNull(message = "인상깊은 대사를 입력해주세요.")
    private String dialogue;

    @ApiModelProperty(value = "리뷰 내용", required = true, example = "좋아요")
    @NotNull(message = "리뷰를 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "공개 여부", required = true, example = "true")
    private boolean revealed;

    public ReviewRequestData() {
        revealed = true;
    }

}
