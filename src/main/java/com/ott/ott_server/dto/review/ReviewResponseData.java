package com.ott.ott_server.dto.review;

import com.ott.ott_server.domain.enums.Gender;
import com.ott.ott_server.dto.movie.MovieResponseData;
import com.ott.ott_server.dto.user.UserOttResponseData;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ApiModel(value="리뷰 등록 요청 정보")
public class ReviewResponseData {

    @ApiModelProperty(value = "리뷰 식별자", example = "1")
    private Long id;

    @ApiModelProperty(value = "사용자 식별자", example = "1")
    private Long userId;

    @ApiModelProperty(value = "사용자 닉네임", example = "뭐라고 짓지")
    private String nickname;

    @ApiModelProperty(value = "사용자 프로필 이미지", example = "image.com")
    private String imageUrl;

    @ApiModelProperty(value = "사용자 성별")
    private Gender gender;

    @ApiModelProperty(value = "생년월일", example = "2001")
    private String birth;

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
