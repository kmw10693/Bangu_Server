package com.ott.ott_server.utils;

import com.ott.ott_server.domain.User;
import com.ott.ott_server.errors.UserNotFoundException;
import com.ott.ott_server.infra.UserRepository;
import com.ott.ott_server.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserUtil {

    private final UserRepository userRepository;

    public User findCurrentUser() {
        User user = userRepository.findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new UserNotFoundException());

        return user;
    }

}
