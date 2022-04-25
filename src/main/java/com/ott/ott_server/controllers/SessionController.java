package com.ott.ott_server.controllers;

import com.ott.ott_server.application.KakaoService;
import com.ott.ott_server.application.SecurityService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.social.KakaoProfile;
import com.ott.ott_server.dto.social.UserSocialLoginRequestDto;
import com.ott.ott_server.dto.social.UserSocialSignupRequestDto;
import com.ott.ott_server.dto.token.TokenDto;
import com.ott.ott_server.dto.token.TokenRequestDto;
import com.ott.ott_server.dto.user.*;
import com.ott.ott_server.errors.CSocialAgreementException;
import com.ott.ott_server.errors.UserNotFoundException;
import com.ott.ott_server.provider.JwtProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "회원가입 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/session")
public class SessionController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityService securityService;
    private final KakaoService kakaoService;
    private final JwtProvider jwtProvider;

    @ApiOperation(value = "로그인", notes = "로그인 요청 data를 받아 로그인을 합니다.")
    @PostMapping("/login")
    public TokenDto login(@ApiParam(value = "로그인 요청 Data", required = true)
                          @RequestBody UserLoginRequestData userLoginRequestData) {
        String email = userLoginRequestData.getEmail();
        String password = userLoginRequestData.getPassword();
        TokenDto tokenDto = userService.login(email, password);

        return tokenDto;
    }

    @ApiOperation(value = "회원가입", notes = "회원가입 요청 data를 받아 회원가입을 합니다.")
    @PostMapping("/signup")
    public UserSignupResponseData signup(
            @ApiParam(value = "회원 가입 요청 Data", required = true)
            @RequestBody @Valid UserRegistrationData userRegistrationData) {
        userRegistrationData.setPassword(passwordEncoder.encode(userRegistrationData.getPassword()));
        User user = userService.signup(userRegistrationData);
        return user.toUserSignupResponseData();
    }

    @ApiOperation(
            value = "액세스, 리프레시 토큰 재발급",
            notes = "엑세스 토큰 만료시 회원 검증 후 리프레쉬 토큰을 검증해서 액세스 토큰과 리프레시 토큰을 재발급합니다.")
    @PostMapping("/reissue")
    public TokenDto reissue(
            @ApiParam(value = "토큰 재발급 요청 DTO", required = true)
            @RequestBody TokenRequestDto tokenRequestDto) {
        return securityService.reissue(tokenRequestDto);
    }


    @ApiOperation(
            value = "소셜 회원가입 - kakao",
            notes = "카카오로 회원가입을 합니다."
    )
    @PostMapping("/social/signup/kakao")
    @ResponseStatus(HttpStatus.CREATED)
    public Long signupBySocial(
            @ApiParam(value = "소셜 회원가입 dto", required = true)
            @RequestBody UserSocialSignupRequestDto socialSignupRequestDto) {

        KakaoProfile kakaoProfile =
                kakaoService.getKakaoProfile(socialSignupRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new UserNotFoundException();
        if (kakaoProfile.getKakao_account().getEmail() == null) {
            kakaoService.kakaoUnlink(socialSignupRequestDto.getAccessToken());
            throw new CSocialAgreementException();
        }

        Long userId = userService.socialSignup(UserSocialRegistrationData.builder()
                .email(kakaoProfile.getKakao_account().getEmail())
                .nickname(kakaoProfile.getProperties().getNickname())
                .provider("kakao")
                .build());

        return userId;
    }

    @ApiOperation(
            value = "소셜 로그인 - kakao",
            notes = "카카오로 로그인을 합니다.")
    @PostMapping("/social/login/kakao")
    @ResponseStatus(HttpStatus.OK)
    public TokenDto loginByKakao(
            @ApiParam(value = "소셜 로그인 dto", required = true)
            @RequestBody UserSocialLoginRequestDto socialLoginRequestDto) {

        KakaoProfile kakaoProfile = kakaoService.getKakaoProfile(socialLoginRequestDto.getAccessToken());
        if (kakaoProfile == null) throw new UserNotFoundException();

        User user = userService.findByEmailAndProvider(kakaoProfile.getKakao_account().getEmail(),
                "kakao");

        return jwtProvider.createTokenDto(user.getId(), user.getRoles());
    }

}
