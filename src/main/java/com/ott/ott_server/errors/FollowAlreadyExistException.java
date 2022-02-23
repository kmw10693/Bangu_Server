package com.ott.ott_server.errors;

public class FollowAlreadyExistException extends RuntimeException {

    public FollowAlreadyExistException() { super("이미 팔로우한 계정입니다!"); }

}
