package com.ott.ott_server.errors;

public class CCommunicationException extends RuntimeException {
    public CCommunicationException() { super("Social 인증 과정에서 문제가 발생했습니다."); }
}
