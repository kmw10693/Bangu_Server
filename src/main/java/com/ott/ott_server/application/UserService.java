package com.ott.ott_server.application;

import antlr.Token;
import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.domain.RefreshToken;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.UserOtt;
import com.ott.ott_server.dto.token.TokenDto;
import com.ott.ott_server.dto.user.UserLoginResponseData;
import com.ott.ott_server.dto.user.UserModificationData;
import com.ott.ott_server.dto.user.UserRegistrationData;
import com.ott.ott_server.dto.user.UserSocialRegistrationData;
import com.ott.ott_server.errors.*;
import com.ott.ott_server.infra.OttRepository;
import com.ott.ott_server.infra.RefreshTokenRepository;
import com.ott.ott_server.infra.UserOttRepository;
import com.ott.ott_server.infra.UserRepository;
import com.ott.ott_server.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Check;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserOttRepository userOttRepository;
    private final OttRepository ottRepository;
    private final Mapper mapper;
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
        String email = userRegistrationData.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }
        String nickname = userRegistrationData.getNickname();
        if (userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicationException(nickname);
        }
        User user = userRepository.save(userRegistrationData.toEntity());
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
            Optional<Ott> ott = findIdByOttName("netflix");
            setUserOtt(user, ott);
        }
        if (userRegistrationData.isTving()){
            Optional<Ott> ott = findIdByOttName("tving");
            setUserOtt(user, ott);
        }
        if(userRegistrationData.isWatcha()) {
            Optional<Ott> ott = findIdByOttName("watcha");
            setUserOtt(user, ott);
        }
        if(userRegistrationData.isWavve()) {
            Optional<Ott> ott = findIdByOttName("wavve");
            setUserOtt(user, ott);
        }

    }

    public User findByEmailAndProvider(String email, String provider) {
        return userRepository.findByEmailAndProvider(email, provider)
                .orElseThrow(UserNotFoundException::new);
    }

    private void setUserOtt(User user, Optional<Ott> ott) {
        userOttRepository.save(UserOtt.builder()
                        .user(user)
                        .ott(ott.get())
                .build());
    }

    private Optional<Ott> findIdByOttName(String title) {
        return ottRepository.findByNameContaining(title);
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
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
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
     * 사용자 업데이트
     *
     * @param id
     * @param modificationData
     * @return
     */
    public User updateUser(Long id, UserModificationData modificationData, String email) throws AccessDeniedException {

        User user = findUserByEmail(email);
        if (!id.equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        modificationData.setPassword(passwordEncoder.encode(modificationData.getPassword()));
        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    /**
     * 사용자 삭제
     *
     * @param id
     * @return
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }
}
