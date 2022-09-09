package com.ott.ott_server.application;

import com.ott.ott_server.domain.RefreshToken;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.token.response.TokenDto;
import com.ott.ott_server.dto.token.request.TokenRequestDto;
import com.ott.ott_server.errors.RefreshTokenException;
import com.ott.ott_server.errors.UserNotFoundException;
import com.ott.ott_server.infra.RefreshTokenRepository;
import com.ott.ott_server.infra.UserRepository;
import com.ott.ott_server.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SecurityService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 만료된 refresh token 에러
        if (!jwtProvider.validationToken(tokenRequestDto.getRefreshToken())) {
            throw new RefreshTokenException();
        }

        // AccessToken 에서 Username (pk) 가져오기
        String accessToken = tokenRequestDto.getAccessToken();
        Authentication authentication = jwtProvider.getAuthentication(accessToken);

        // user pk로 유저 검색 / repo 에 저장된 Refresh Token 이 없음
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(UserNotFoundException::new);
        RefreshToken refreshToken = tokenRepository.findByKey(user.getId())
                .orElseThrow(RefreshTokenException::new);

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken()))
            throw new RefreshTokenException();

        // AccessToken, RefreshToken 토큰 재발급, 리프레쉬 토큰 저장
        TokenDto newCreatedToken = jwtProvider.createTokenDto(user.getId(), user.getRoles());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newCreatedToken.getRefreshToken());
        tokenRepository.save(updateRefreshToken);

        return newCreatedToken;
    }

}
