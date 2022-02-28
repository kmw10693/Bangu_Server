package com.ott.ott_server.errors;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("유저 아이디를 찾을 수 없습니다! " + id);
    }

    public UserNotFoundException(String username) {
        super("유저 아이디를 찾을 수 없습니다! " + username);
    }

    public UserNotFoundException() {
        super("유저 아이디를 찾을 수 없습니다! ");
    }
}
