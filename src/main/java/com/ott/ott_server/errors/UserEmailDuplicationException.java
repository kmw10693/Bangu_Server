package com.ott.ott_server.errors;

public class UserEmailDuplicationException extends RuntimeException {
    public UserEmailDuplicationException(String email) {
        super("유저 아이디가 이미 존재합니다! "+email);
    }
}
