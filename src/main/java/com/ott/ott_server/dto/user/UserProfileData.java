package com.ott.ott_server.dto.user;

import com.ott.ott_server.domain.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@ApiModel("사용자 프로필 정보")
public class UserProfileData {
    @ApiModelProperty(value = "사용자 식별자", example = "1")
    private Long id;

    @ApiModelProperty(value = "사용자 닉네임", example = "가나다")
    private String nickname;

    @ApiModelProperty(value = "사용자 프로필 이미지", example = "www.img.com")
    private String imageUrl;

    @ApiModelProperty(value = "사용자 성별", example = "M")
    private Gender gender;

    @ApiModelProperty(value = "나이대 예시) 20대 2", example = "2")
    private Long birth;

}
