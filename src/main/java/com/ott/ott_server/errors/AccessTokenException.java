package com.ott.ott_server.errors;

public class AccessTokenException extends RuntimeException {
    public AccessTokenException() {
        super("엑세스 토큰이 만료되지 않았거나 유효하지 않은 엑세스 토큰입니다.");
    }
}
