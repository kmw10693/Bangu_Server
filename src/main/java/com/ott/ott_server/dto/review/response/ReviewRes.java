package com.ott.ott_server.dto.review.response;

import com.ott.ott_server.dto.user.response.UserProfileData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value="리뷰 반환 정보")
public class ReviewRes {

    @ApiModelProperty(value = "리뷰 식별자", example = "1")
    private Long id;

    private UserProfileData userProfileData;

    @ApiModelProperty(value = "팔로우 여부", example = "true")
    private Boolean followState;

    @ApiModelProperty(value = "북마크 여부", example = "true")
    private Boolean bookMark;

    @ApiModelProperty(value = "영화 제목", example = "셜록")
    private String title;

    @ApiModelProperty(value = "장르", example = "드라마")
    private String genre;

    @ApiModelProperty(value = "영화 이미지", example = "image.com")
    private String imageUrl;

    @ApiModelProperty(value = "별점", example = "4.5")
    private BigDecimal score;

    @ApiModelProperty(value = "리뷰 내용", example = "좋아요")
    private String content;

    private List<ReviewOttResponseData> reviewOtts;

}
