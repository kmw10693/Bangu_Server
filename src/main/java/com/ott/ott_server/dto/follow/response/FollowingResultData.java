package com.ott.ott_server.dto.follow.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@ApiModel("유저 팔로잉 데이터")
public class FollowingResultData {
    @ApiModelProperty(value = "팔로잉 수", example = "15")
    private int followings;

    private Page<FollowData> followData;

}
