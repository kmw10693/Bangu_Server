package com.ott.ott_server.dto.follow.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;


@Getter
@Setter
@Builder
@AllArgsConstructor
@ApiModel("유저 팔로워 데이터")
public class FollowResultData {

    @ApiModelProperty(value = "팔로워 수", example = "15")
    private int followers;

    private Page<FollowData> followData;
}
