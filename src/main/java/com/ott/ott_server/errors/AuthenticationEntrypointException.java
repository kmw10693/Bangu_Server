package com.ott.ott_server.errors;

public class AuthenticationEntrypointException extends RuntimeException {
    public AuthenticationEntrypointException() { super("해당 리소스에 접근할 권한이 없습니다."); }
}
