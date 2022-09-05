package com.ott.ott_server.dto.token;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequestDto {

    @ApiModelProperty(value = "갱신할 엑세스 토큰", required = true, example = "a9ace025c90c0da22sf161075da6ddd3492a2fca776")
    String accessToken;

    @ApiModelProperty(value = "리프레시 토큰", required = true, example = "a9ace025c9053aa09fs8dcc0da2161075da6ddd3492a2fca776")
    String refreshToken;

}
