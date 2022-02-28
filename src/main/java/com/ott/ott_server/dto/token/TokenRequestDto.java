package com.ott.ott_server.dto.token;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRequestDto {
    String accessToken;
    String refreshToken;

}