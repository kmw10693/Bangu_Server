package com.ott.ott_server.dto.follow.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@ApiModel("유저 팔로워 조회 데이터")
public class FollowData {

    @ApiModelProperty(value = "사용자 인덱스", example = "1")
    private Long userId;

    @ApiModelProperty(value = "사용자 닉네임" , example = "반구반구")
    private String nickname;

    @ApiModelProperty(value = "팔로우 여부", example = "true")
    private boolean followState;

    @ApiModelProperty(value = "사용자 프로필 이미지", example = "image.com")
    private String imageUrl;

    @ApiModelProperty(value = "현재 사용자가 팔로워에 있는지 여부", example = "true")
    private boolean loginUser;


    public FollowData() {
        followState = false;
        loginUser = false;
    }

    public void setFollowState(boolean followState) {
        this.followState = followState;
    }

    public void setLoginUser(boolean loginUser) {
        this.loginUser = loginUser;
    }
}
