package com.ott.ott_server.dto.token.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenDto {
    private String grantType;
    @ApiModelProperty(value = "엑세스 토큰")
    private String accessToken;
    @ApiModelProperty(value = "리프레쉬 토큰")
    private String refreshToken;
    @ApiModelProperty(value = "엑세스 토큰 만료 기한(1시간)ms")
    private Long accessTokenExpireDate;
}
