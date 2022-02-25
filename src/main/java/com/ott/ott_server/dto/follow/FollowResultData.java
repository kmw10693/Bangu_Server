package com.ott.ott_server.dto.follow;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
public class FollowResultData {

    @ApiParam(value = "사용자 idx", required = true, example = "1")
    private Long userId;

    @ApiParam(value = "사용자 프로필 이미지", required = true, example = "image.com")
    private String imageUrl;

    @ApiParam(value = "현 사용자가 팔로우 관계인지 확인 여부", required = true, example = "true")
    private boolean followState;

    @ApiParam(value = "현 사용자가 팔로워에 있는지 확인 여부", required = true, example = "true")
    private boolean loginUser;

    @ApiParam(value = "사용자 닉네임" , required = true, example = "닉네임")
    private String nickname;

    public FollowResultData() {
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
