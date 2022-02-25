package com.ott.ott_server.dto.review;

import com.ott.ott_server.dto.movie.MovieRequestData;
import com.ott.ott_server.dto.movie.MovieResponseData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(value="리뷰 등록 요청 정보")
public class ReviewRequestData {

    @ApiModelProperty(value = "별점", required = true, example = "4.5")
    @NotNull(message = "영화 식별자를 입력해주세요.")
    private Long movieId;

    @ApiParam(value = "tving 선택 여부", required = true, example = "true")
    private boolean tving;

    @ApiParam(value = "watcha 선택 여부", required = true, example = "true")
    private boolean watcha;

    @ApiParam(value = "netflix 선택 여부", required = true, example = "true")
    private boolean netflix;

    @ApiParam(value = "wavve 선택 여부", required = true, example = "true")
    private boolean wavve;

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
        tving = false;
        netflix = false;
        watcha = false;
        wavve = false;
    }


}
