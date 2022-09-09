package com.ott.ott_server.dto.review.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value="리뷰 수정 요청 정보")
public class ReviewModificationData {

    @ApiModelProperty(value = "감상 포인트", required = true, example = "좋아요")
    @NotBlank(message = "감상 포인트를 입력해주세요.")
    private String attention;

    @ApiModelProperty(value = "인상깊은 대사", required = true, example = "기억이 안난다.")
    @NotBlank(message = "인상깊은 대사를 입력해주세요.")
    private String dialogue;

    @ApiModelProperty(value = "리뷰 내용", required = true, example = "좋아요")
    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "별점", required = true, example = "4.5")
    @DecimalMin("0") @DecimalMax("5")
    @NotNull(message = "별점을 입력해주세요.")
    private BigDecimal score;

    @ApiModelProperty(value = "공개 여부", required = true, example = "true")
    @NotNull
    private boolean revealed;

}
