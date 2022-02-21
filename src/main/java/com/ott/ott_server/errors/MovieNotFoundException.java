package com.ott.ott_server.errors;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException(Long id) {
        super("영화가 존재하지 않습니다! " + id);
    }
}
