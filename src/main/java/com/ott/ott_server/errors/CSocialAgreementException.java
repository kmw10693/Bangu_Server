package com.ott.ott_server.errors;

public class CSocialAgreementException extends RuntimeException {

    public CSocialAgreementException() {
        super("소셜 로그인 시 필수 동의항목 미동의시 에러");
    }
}
