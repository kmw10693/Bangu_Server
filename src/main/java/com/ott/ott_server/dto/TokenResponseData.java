package com.ott.ott_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class TokenResponseData {
    private String token;

    public TokenResponseData(String token) {
        this.token = token;
    }
}
