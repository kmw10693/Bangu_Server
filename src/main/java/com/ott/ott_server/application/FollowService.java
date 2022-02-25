package com.ott.ott_server.application;

import com.ott.ott_server.domain.Follow;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.follow.FollowResultData;
import com.ott.ott_server.errors.FollowAlreadyExistException;
import com.ott.ott_server.infra.FollowRepository;
import com.ott.ott_server.infra.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

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

    public List<FollowResultData> getFollower(Long userId, Long loginId) {
        List<Follow> followers = followRepository.findByToUserId(userId);
        List<FollowResultData> followResultData = new ArrayList<>();

        for (Follow follower : followers) {
            boolean loginUser = false;
            boolean followState = false;

            // 만약 로그인한 사용자(자신)이 팔로워 목록에 포함되어 있다면
            // from은 팔로워 to는 팔로우
            if(follower.getFromUser().getId() == loginId) {
                loginUser = true;
            }

            // 만약 로그인한 사용자(자신)이 다른 사용자(user)의 팔로워를 보는 경우
            if(followRepository.existsByFromUserIdAndToUserId(loginId, follower.getFromUser().getId())) {
                followState = true;
            }

            followResultData.add(
                    FollowResultData.builder()
                            .userId(follower.getFromUser().getId())
                            .imageUrl(follower.getToUser().getImageUrl())
                            .nickname(follower.getToUser().getNickname())
                            .loginUser(loginUser)
                            .followState(followState)
                            .build()
            );
        }

        return followResultData;
    }

    public List<FollowResultData> getFollowing(Long userId, Long loginId) {

        List<Follow> followers = followRepository.findByFromUserId(userId);
        List<FollowResultData> followResultData = new ArrayList<>();

        for (Follow follower : followers) {
            boolean loginUser = false;
            boolean followState = false;

            // 만약 로그인한 사용자(자신)이 팔로워 목록에 포함되어 있다면
            // from은 팔로워 to는 팔로우
            if(follower.getToUser().getId() == loginId) {
                loginUser = true;
            }

            // 만약 로그인한 사용자(자신)이 다른 사용자(user)의 팔로워를 보는 경우
            if(followRepository.existsByFromUserIdAndToUserId(loginId, follower.getToUser().getId())) {
                followState = true;
            }

            followResultData.add(
                    FollowResultData.builder()
                            .userId(follower.getToUser().getId())
                            .imageUrl(follower.getToUser().getImageUrl())
                            .nickname(follower.getToUser().getNickname())
                            .loginUser(loginUser)
                            .followState(followState)
                            .build()
            );
        }

        return followResultData;

    }



}
