package com.ott.ott_server.dto.token.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequestDto {

    @ApiModelProperty(value = "갱신할 엑세스 토큰", required = true, example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwicm9sZXMiOlsiUk9MRV9VU0VSIl0sImlhdCI6MTY2Mjk5MTc0MywiZXhwIjoxNjYyOTkxNzQzfQ.aKZ8Rs3IRZt8ZjkBwImqqD7giCAgTLPyVqnD-jpH_")
    private String accessToken;

    @ApiModelProperty(value = "리프레시 토큰", required = true, example = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NjQyMDA5MDh9.YbBY2zW7wDBDrqSAPV3uilc90MsQxHoAx8CQX-yRfA0")
    private String refreshToken;

}
