package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.user.UserLoginResponseData;
import com.ott.ott_server.dto.user.UserModificationData;
import com.ott.ott_server.dto.user.UserRegistrationData;
import com.ott.ott_server.errors.EmailLoginFailedException;
import com.ott.ott_server.errors.UserEmailDuplicationException;
import com.ott.ott_server.errors.UserNickNameDuplicationException;
import com.ott.ott_server.errors.UserNotFoundException;
import com.ott.ott_server.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.AccessDeniedException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 정보를 DB에 저장합니다.
     * @param userRegistrationData
     * @return
     */
    public User signup(UserRegistrationData userRegistrationData) {
        String email = userRegistrationData.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }
        String nickname = userRegistrationData.getNickname();
        if(userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicationException(nickname);
        }
        return userRepository.save(userRegistrationData.toEntity());
    }

    /**
     * 중복 아이디 확인
     * @param userEmail
     * @return
     */
    public Boolean isDuplicateEmail(String userEmail) {
        if(userRepository.existsByEmail(userEmail)) {
            throw new UserEmailDuplicationException(userEmail);
        } else {
            return false;
        }
    }

    /**
     * 유저 닉네임 중복 확인
     */
    public Boolean isDuplicateNickname(String userNickName) {
        if(userRepository.existsByNickname(userNickName)) {
            throw new UserNickNameDuplicationException(userNickName);
        } else {
            return false;
        }
    }

    /**
     * 사용자가 로그인합니다.
     * @param email
     * @param password
     * @return
     */
    public UserLoginResponseData login(String email, String password) {
        User user = userRepository.findByEmail(email).orElseThrow(EmailLoginFailedException::new);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new EmailLoginFailedException();
        return user.toUserLoginResponseData();
    }

    /**
     * 사용자를 가져옵니다.
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
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    private User findUserByEmail(String username) {
        return userRepository.findByEmailAndDeletedIsFalse(username)
                .orElseThrow(()-> new UserNotFoundException(username));
    }

    /**
     * 사용자 업데이트
     * @param id
     * @param modificationData
     * @return
     */
    public User updateUser(Long id, UserModificationData modificationData, String email) throws AccessDeniedException {

        User user = findUserByEmail(email);
        if(!id.equals(user.getId())) {
            throw new AccessDeniedException("Access denied");
        }

        User updateUser = findUser(id);
        modificationData.setPassword(passwordEncoder.encode(modificationData.getPassword()));
        User source = mapper.map(modificationData, User.class);
        updateUser.changeWith(source);

        return user;
    }

    /**
     * 사용자 삭제
     * @param id
     * @return
     */
    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }
}
