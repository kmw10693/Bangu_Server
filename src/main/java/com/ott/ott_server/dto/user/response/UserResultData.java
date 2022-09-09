package com.ott.ott_server.dto.user.response;

import com.ott.ott_server.domain.enums.Gender;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@ApiModel(value = "유저 등록 성공 정보")
public class UserResultData {
    @ApiModelProperty(value = "사용자 인덱스", example = "1")
    private Long id;

    @ApiModelProperty(value = "사용자 이메일", example = "test@email.com")
    private String email;

    @ApiModelProperty(value = "사용자 닉네임", example = "bangu")
    private String nickname;

    @ApiModelProperty(value = "나이대 ex) 20대 2", example = "2")
    private Long birth;

    @ApiModelProperty(value = "성별", example = "M")
    private Gender gender;

    @Builder.Default
    private List<UserOttResponseData> userOttResponseData = new ArrayList<>();

    @ApiModelProperty(value = "사용자 생성 시각", example = "2022-01-24T16:19:01.359146")
    private LocalDateTime create_at;

    @ApiModelProperty(value = "사용자 수정 시각", example = "2022-01-24T16:19:01.359146")
    private LocalDateTime update_at;

}
