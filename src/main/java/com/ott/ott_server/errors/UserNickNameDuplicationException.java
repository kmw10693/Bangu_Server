package com.ott.ott_server.errors;

public class UserNickNameDuplicationException extends RuntimeException {
    public UserNickNameDuplicationException(String nickname) {
        super("유저 닉네임이 이미 존재합니다! "+ nickname);
    }
}
