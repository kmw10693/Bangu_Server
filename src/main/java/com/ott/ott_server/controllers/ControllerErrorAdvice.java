package com.ott.ott_server.controllers;

import com.ott.ott_server.errors.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseBody
@ControllerAdvice
public class ControllerErrorAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound() {
        return new ErrorResponse("유저를 찾을 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MovieNotFoundException.class)
    public ErrorResponse handleMovieNotFound() {
        return new ErrorResponse("영화를 찾을 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ReviewNotFoundException.class)
    public ErrorResponse handleReviewNotFound() {
        return new ErrorResponse("리뷰를 찾을 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExists() {
        return new ErrorResponse("유저 이메일이 이미 존재합니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNickNameDuplicationException.class)
    public ErrorResponse handleUserNickNameIsAlreadyExists() {
        return new ErrorResponse("유저 닉네임이 이미 존재합니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EmailLoginFailedException.class)
    public ErrorResponse handleEmailLoginFailed() {
        return new ErrorResponse("아이디나 비밀번호가 맞는지 확인해주세요.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNotMatchException.class)
    public ErrorResponse handleUserNotMatch() {
        return new ErrorResponse("리뷰 작성자가 일치하지 않습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FollowAlreadyExistException.class)
    public ErrorResponse handleFollowAlreadyExist() {
        return new ErrorResponse("이미 팔로우한 계정입니다!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AuthenticationEntrypointException.class)
    public ErrorResponse handleAuthenticationEntryPoint() {
        return new ErrorResponse("해당 리소스에 접근할 권한이 없습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccessDeniedException.class)
    public ErrorResponse handleAccessDeniedPoint() {
        return new ErrorResponse("이 리소스에 대한 접근 권한이 없습니다.");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RefreshTokenException.class)
    public ErrorResponse handleRefreshTokenPoint() {
        return new ErrorResponse("유효하지 않은 리프레시 토큰입니다. 재로그인 해주세요.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CCommunicationException.class)
    public ErrorResponse handleCommunication() {
        return new ErrorResponse("Social 인증 과정에서 에러가 발생했습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CSocialAgreementException.class)
    public ErrorResponse handleSocialAgreement() {
        return new ErrorResponse("소셜 로그인 시 필수 동의항목 미동의 하였습니다.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CUserExistException.class)
    public ErrorResponse handleUserExist() {
        return new ErrorResponse("이미 가입된 소셜 계정입니다!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OttNameNotFoundException.class)
    public ErrorResponse handleOttNameNotFound() {
        return new ErrorResponse("해당하는 OTT가 없습니다!");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GenreNotFoundException.class)
    public ErrorResponse handleGenreNotFound() {
        return new ErrorResponse("해당하는 장르가 없습니다!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BookmarkUserException.class)
    public ErrorResponse handleBookMarkBadRequest() {
        return new ErrorResponse("자기의 리뷰를 북마크할 수 없습니다.");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(FollowNotFoundException.class)
    public ErrorResponse handleFollowingNotFound() {
        return new ErrorResponse("팔로우 되어 있지 않는 사용자 입니다!");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(OttNameDuplicationException.class)
    public ErrorResponse handleOttNameAlreadyExist() {
        return new ErrorResponse("해당하는 OTT 이름이 존재합니다!");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AccessTokenException.class)
    public ErrorResponse handleAccessTokenPoint() {
        return new ErrorResponse("엑세스 토큰이 만료되지 않았거나 유효하지 않은 엑세스 토큰입니다.");
    }

}
