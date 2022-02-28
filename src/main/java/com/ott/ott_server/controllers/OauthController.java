package com.ott.ott_server.controllers;

import com.ott.ott_server.application.KakaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

@Api(tags = "OAuth 2.0 Kakao")
@Controller
@RequiredArgsConstructor
@RequestMapping("/oauth/kakao")
public class OauthController {

    private final RestTemplate restTemplate;
    private final Environment env;
    private final KakaoService kakaoService;

    @Value("${spring.url.base}")
    private String baseUrl;

    @Value("${social.kakao.client-id}")
    private String kakaoClientId;

    @Value("${social.kakao.redirect}")
    private String kakaoRedirectUri;

    @GetMapping("/login")
    public ModelAndView socialLogin(ModelAndView mav) {

        StringBuilder loginUri = new StringBuilder()
                .append(env.getProperty("social.kakao.url.login"))
                .append("?response_type=code")
                .append("&client_id=").append(kakaoClientId)
                .append("&redirect_uri=").append(baseUrl).append(kakaoRedirectUri);
        mav.addObject("loginUrl", loginUri);
        mav.setViewName("social/login");
        return mav;
    }

    @GetMapping(value = "/redirect")
    public ModelAndView redirectKakao(
            ModelAndView mav,
            @ApiParam(value = "Authorization Code", required = true)
            @RequestParam String code) {

        mav.addObject("authInfo", kakaoService.getKakaoTokenInfo(code));
        mav.setViewName("social/redirectKakao");
        return mav;
    }
}
