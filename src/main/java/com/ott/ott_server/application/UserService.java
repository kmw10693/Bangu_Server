package com.ott.ott_server.application;

import com.github.dozermapper.core.Mapper;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.UserModificationData;
import com.ott.ott_server.dto.UserRegistrationData;
import com.ott.ott_server.errors.UserEmailDuplicationException;
import com.ott.ott_server.errors.UserNickNameDuplicationException;
import com.ott.ott_server.errors.UserNotFoundException;
import com.ott.ott_server.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    public User registerUser(UserRegistrationData userRegistrationData) {
        String email = userRegistrationData.getEmail();
        if(userRepository.existsByEmail(email)) {
            throw new UserEmailDuplicationException(email);
        }
        String nickname = userRegistrationData.getNickname();
        if(userRepository.existsByNickname(nickname)) {
            throw new UserNickNameDuplicationException(nickname);
        }
        // 비밀번호 복호화
        String encodedPassword = passwordEncoder.encode(userRegistrationData.getPassword());
        User user = mapper.map(userRegistrationData, User.class);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    public User getUser(Long id) {
        return findUser(id);
    }

    private User findUser(Long id) {
        return userRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(()-> new UserNotFoundException(id));
    }

    public User updateUser(Long id, UserModificationData modificationData) {

        User user = findUser(id);

        User source = mapper.map(modificationData, User.class);
        user.changeWith(source);

        return user;
    }

    public User deleteUser(Long id) {
        User user = findUser(id);
        user.destroy();
        return user;
    }
}
