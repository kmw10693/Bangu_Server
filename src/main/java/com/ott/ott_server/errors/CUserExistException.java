package com.ott.ott_server.errors;

public class CUserExistException extends RuntimeException {

    public CUserExistException() {
        super("이미 가입된 소셜 계정입니다!");
    }
}
