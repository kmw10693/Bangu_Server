package com.ott.ott_server.errors;

public class RefreshTokenException extends RuntimeException {

    public RefreshTokenException() {
        super("유효하지 않은 리프레시 토큰입니다. 재로그인 해주세요.");
    }
}
