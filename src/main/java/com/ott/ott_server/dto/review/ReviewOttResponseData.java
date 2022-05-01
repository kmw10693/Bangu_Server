package com.ott.ott_server.dto.review;

import com.ott.ott_server.domain.ReviewOtt;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(value = "리뷰 OTT 반환 정보")
public class ReviewOttResponseData {

    @ApiModelProperty(value = "리뷰의 ott 인덱스", example = "1")
    private Long id;

    @ApiModelProperty(value = "리뷰의 선택한 OTT 명", example = "WAVVE")
    private String ottName;

    public static List<ReviewOttResponseData> of(List<ReviewOtt> otts) {
        return otts.stream().map(ReviewOttResponseData::of).collect(Collectors.toList());
    }

    public static ReviewOttResponseData of(ReviewOtt reviewOtt) {
        return ReviewOttResponseData.builder()
                .id(reviewOtt.getOtt().getId())
                .ottName(reviewOtt.getOtt().getName())
                .build();
    }

}
