package com.ott.ott_server.errors;

public class UserNotMatchException extends RuntimeException{
    public UserNotMatchException() {
        super("리뷰의 유저 아이디와 요청하는 유저 아이디가 일치하지 않습니다!");
    }
}
