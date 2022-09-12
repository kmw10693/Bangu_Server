package com.ott.ott_server.dto.review.response;

import com.ott.ott_server.dto.movie.response.MovieResponseData;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value="리뷰 검색 반환 정보")
public class ReviewSearchData {

    private List<MovieResponseData> movieResponseData;

    private Page<ReviewRes> reviewRes;

}
