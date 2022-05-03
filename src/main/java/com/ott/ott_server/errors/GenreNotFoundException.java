package com.ott.ott_server.errors;

public class GenreNotFoundException extends RuntimeException {
    public GenreNotFoundException() {
        super("해당 장르가 없습니다.");
    }
}
