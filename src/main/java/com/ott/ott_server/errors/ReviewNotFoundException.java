package com.ott.ott_server.errors;

public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super("해당 리뷰를 찾을수 없습니다! " + id);
    }
}
