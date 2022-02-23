package com.ott.ott_server.application;

import com.ott.ott_server.domain.Follow;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.errors.FollowAlreadyExistException;
import com.ott.ott_server.infra.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;

    private void checkFollow(Long fromUserId, Long toUserId) {
        if (followRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            throw new FollowAlreadyExistException();
        }
    }

    public void follow(User fromUser, User toUser) {
        checkFollow(fromUser.getId(), toUser.getId());
        followRepository.save(
                Follow.builder()
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .build()
        );
    }

    public void unFollow(User fromUser, User toUser) {
        Optional<Follow> follow = followRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
        followRepository.deleteById(follow.get().getId());
    }
}
