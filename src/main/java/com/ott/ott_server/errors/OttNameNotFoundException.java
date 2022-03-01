package com.ott.ott_server.errors;

public class OttNameNotFoundException extends RuntimeException {

    public OttNameNotFoundException() {
        super("해당하는 ott가 없습니다!");
    }
}
