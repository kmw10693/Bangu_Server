package com.ott.ott_server.dto.review.response;

import com.ott.ott_server.dto.movie.response.MovieResponseData;
import com.ott.ott_server.dto.user.response.UserProfileData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value="리뷰 등록 결과 정보")
public class ReviewResultData {

    @ApiModelProperty(value = "리뷰 식별자", example = "1")
    private Long id;

    private UserProfileData userProfileData;

    private MovieResponseData movieResponseData;

    @Builder.Default
    private List<ReviewOttResponseData> reviewOttResponseData = new ArrayList<>();

    @ApiModelProperty(value = "별점", example = "4.5")
    private BigDecimal score;

    @ApiModelProperty(value = "감상 포인트", example = "좋아요")
    private String attention;

    @ApiModelProperty(value = "인상깊은 대사", example = "기억이 안난다.")
    private String dialogue;

    @ApiModelProperty(value = "리뷰 내용", example = "좋아요")
    private String content;

}
