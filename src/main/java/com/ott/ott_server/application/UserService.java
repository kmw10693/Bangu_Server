package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.domain.RefreshToken;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.UserOtt;
import com.ott.ott_server.domain.enums.OttNames;
import com.ott.ott_server.dto.token.response.TokenDto;
import com.ott.ott_server.dto.user.request.UserRegistrationData;
import com.ott.ott_server.dto.user.request.UserPasswordModifyData;
import com.ott.ott_server.dto.user.response.UserSocialRegistrationData;
import com.ott.ott_server.errors.*;
import com.ott.ott_server.infra.OttRepository;
import com.ott.ott_server.infra.RefreshTokenRepository;
import com.ott.ott_server.infra.UserOttRepository;
import com.ott.ott_server.infra.UserRepository;
import com.ott.ott_server.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserOttRepository userOttRepository;
    private final OttRepository ottRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    /**
     * 사용자 정보를 DB에 저장합니다.
     *
     * @param userRegistrationData
     * @return
     */
    public User signup(UserRegistrationData userRegistrationData) {
        String email = userRegistrationData.getUserId();
        String age = userRegistrationData.getBirth().substring(0, 4);
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }
        String nickname = userRegistrationData.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicationException(nickname);
        }
        User user = userRepository.save(userRegistrationData.toEntity());
        user.setBirth((2023 - Long.valueOf(age)) / 10);
        checkSubscribe(userRegistrationData, user);
        return user;
    }

    // 소셜 회원가입 - 패스워드가 필요없으므로 따로 만듬
    public Long socialSignup(UserSocialRegistrationData userSignupRequestDto) {
        if (userRepository
                .findByEmailAndProvider(userSignupRequestDto.getEmail(), userSignupRequestDto.getProvider())
                .isPresent()
        ) throw new CUserExistException();
        return userRepository.save(userSignupRequestDto.toEntity()).getId();
    }

    private void checkSubscribe(UserRegistrationData userRegistrationData, User user) {
        if (userRegistrationData.isNetflix()) {
            Ott ott = findByOttName(OttNames.NETFLIX.value());
            setUserOtt(user, ott);
        }
        if (userRegistrationData.isTving()) {
            Ott ott = findByOttName(OttNames.TVING.value());
            setUserOtt(user, ott);
        }
        if (userRegistrationData.isWatcha()) {
            Ott ott = findByOttName(OttNames.WATCHA.value());
            setUserOtt(user, ott);
        }
        if (userRegistrationData.isWavve()) {
            Ott ott = findByOttName(OttNames.WAVVE.value());
            setUserOtt(user, ott);
        }

    }

    public User findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(UserNotFoundException::new);
    }

    private void setUserOtt(User user, Ott ott) {
        userOttRepository.save(UserOtt.builder()
                .user(user)
                .ott(ott)
                .build());
    }

    private Ott findByOttName(String title) {
        return ottRepository.findByName(title)
                .orElseThrow(OttNameNotFoundException::new);
    }

    /**
     * 중복 아이디 확인
     *
     * @param userEmail
     * @return
     */
    public Boolean isDuplicateEmail(String userEmail) {
        if (userRepository.existsByEmail(userEmail)) {
            throw new UserEmailDuplicationException(userEmail);
        } else {
            return false;
        }
    }

    /**
     * 유저 닉네임 중복 확인
     */
    public Boolean isDuplicateNickname(String userNickName) {
        if (userRepository.existsByNickname(userNickName)) {
            throw new UserNickNameDuplicationException(userNickName);
        } else {
            return false;
        }
    }

    /**
     * 사용자가 로그인합니다.
     *
     * @param email
     * @param password
     * @return
     */
    public TokenDto login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(EmailLoginFailedException::new);

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new EmailLoginFailedException();

        // AccessToken, RefreshToken 발급
        TokenDto tokenDto = jwtProvider.createTokenDto(user.getId(), user.getRoles());

        // RefreshToken 저장
        RefreshToken refreshToken = refreshTokenRepository.findByKey(user.getId())
                .orElseGet(() -> refreshTokenRepository.save(RefreshToken.builder()
                        .key(user.getId())
                        .token(tokenDto.getRefreshToken())
                        .build()));

        refreshToken.updateToken(tokenDto.getRefreshToken());
        return tokenDto;
    }

    /**
     * 사용자를 가져옵니다.
     *
     * @param id
     * @return
     */
    public User getUser(Long id) {
        return findUser(id);
    }

    public User getUser(String username) {
        return findUserByEmail(username);
    }

    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    private User findUserByEmail(String username) {
        return userRepository.findByEmailAndDeletedIsFalse(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    /**
     * 비밀번호 업데이트
     */
    public void updatePassword(User user, UserPasswordModifyData userPasswordModifyData) {

        // 비밀번호를 암호화 한다.
        userPasswordModifyData.setPassword(passwordEncoder.encode(userPasswordModifyData.getPassword()));
        // 비밀번호를 업데이트 한다.
        user.updatePassword(userPasswordModifyData.getPassword());
    }

    /**
     * 닉네임 업데이트
     */
    public void updateNickname(User user, String nickname) {
        // DB에 닉네임이 중복되어 있다면(참)
        if (userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicationException(nickname);
        }
        // 변경 후 리턴
        user.updateNickname(nickname);
    }

    /**
     * 이미지 업데이트
     */
    public void updateImageUrl(User user, String imageUrl) {
        user.updateImageUrl(imageUrl);
    }

    /**
     * 사용자 삭제
     *
     * @return
     */
    public void deleteUser(User user) {
        user.destroy();
    }
}
