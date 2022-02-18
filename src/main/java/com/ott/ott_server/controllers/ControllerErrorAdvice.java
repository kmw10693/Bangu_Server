package com.ott.ott_server.controllers;

import com.ott.ott_server.errors.ErrorResponse;
import com.ott.ott_server.errors.UserEmailDuplicationException;
import com.ott.ott_server.errors.UserNickNameDuplicationException;
import com.ott.ott_server.errors.UserNotFoundException;
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
        return new ErrorResponse("User not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserEmailDuplicationException.class)
    public ErrorResponse handleUserEmailIsAlreadyExists() {
        return new ErrorResponse("User's email already exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UserNickNameDuplicationException.class)
    public ErrorResponse handleUserNickNameIsAlreadyExists() {
        return new ErrorResponse("User's nickname already exists");
    }

}
