package com.ott.ott_server.errors;

public class OttNameDuplicationException extends RuntimeException {

    public OttNameDuplicationException(String name) {
        super("해당하는 OTT 이름이 존재합니다!" + name);
    }

}
