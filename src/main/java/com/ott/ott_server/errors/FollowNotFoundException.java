package com.ott.ott_server.errors;

public class FollowNotFoundException extends RuntimeException {

    public FollowNotFoundException() { super("팔로우 되어 있지 않는 사용자 입니다!");}

}
