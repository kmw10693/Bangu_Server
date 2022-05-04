package com.ott.ott_server.errors;

public class BookmarkUserException extends RuntimeException {
    public BookmarkUserException() { super("자신의 글을 북마크할 수 없습니다."); }

}
