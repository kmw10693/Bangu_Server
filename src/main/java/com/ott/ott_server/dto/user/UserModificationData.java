package com.ott.ott_server.dto.user;

import com.github.dozermapper.core.Mapping;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Getter
@AllArgsConstructor
@Builder
@Setter
@ApiModel(value = "유저 수정 요청 정보")
public class UserModificationData {

    @Size(min=3, max=20)
    @Mapping("password")
    @Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{3,}$",
            message = "비밀번호는 영문 대,소문자와 숫자1개 이상씩 포함된 3자 이상의 아이디여야 합니다.")
    @ApiParam(value = "수정할 비밀번호", required = true, example = "testst")
    private String password;

    @Size(max=8)
    @Mapping("nickname")
    @ApiParam(value = "수정할 닉네임", required = true, example = "반구")
    private String nickname;

    @Mapping("imageUrl")
    @ApiParam(value = "수정할 프로필 사진", required = true, example = "image.com")
    private String imageUrl;

}
