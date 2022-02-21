package com.ott.ott_server.dto.user;

import com.github.dozermapper.core.Mapping;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@AllArgsConstructor
@Builder
@Setter
public class UserModificationData {

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min=3, max=20)
    @Mapping("password")
    @ApiParam(value = "수정할 비밀번호", required = true, example = "testst")
    private String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max=8)
    @Mapping("nickname")
    @ApiParam(value = "수정할 닉네임", required = true, example = "반구")
    private String nickname;


}
