package com.ott.ott_server.errors;

public class EmailLoginFailedException extends RuntimeException {
    public EmailLoginFailedException() { super("가입하지 않은 아이디이거나, 잘못된 비밀번호입니다!");}

}