package com.ott.ott_server.application;

import com.ott.ott_server.domain.Follow;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.follow.response.FollowData;
import com.ott.ott_server.dto.follow.response.FollowResultData;
import com.ott.ott_server.dto.follow.response.FollowingResultData;
import com.ott.ott_server.errors.FollowAlreadyExistException;
import com.ott.ott_server.errors.FollowNotFoundException;
import com.ott.ott_server.infra.FollowRepository;
import com.ott.ott_server.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowService {

    private final UserUtil userUtil;
    private final FollowRepository followRepository;

    private boolean checkFollow(Long fromUserId, Long toUserId) {
        if (followRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            return true;
        }
        return false;
    }

    public void follow(User toUser) {
        User fromUser = userUtil.findCurrentUser();

        if (checkFollow(fromUser.getId(), toUser.getId()) == true) {
            throw new FollowAlreadyExistException();
        }
        followRepository.save(
                Follow.builder()
                        .fromUser(fromUser)
                        .toUser(toUser)
                        .build()
        );
    }

    public void unFollow(User toUser) {
        User fromUser = userUtil.findCurrentUser();
        if (checkFollow(fromUser.getId(), toUser.getId()) == false) {
            throw new FollowNotFoundException();
        }

        Follow follow = followRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
        followRepository.deleteById(follow.getId());
    }

    public FollowResultData getFollower(Long userId, Long loginId, String nickname, Pageable pageable) {
        List<Follow> followers = followRepository.findByToUserId(userId);
        List<FollowData> followData = new ArrayList<>();

        for (Follow follower : followers) {
            boolean loginUser = false;
            boolean followState = false;

            // 만약 자신이 팔로워 목록에 포함되어 있다면
            if (follower.getFromUser().getId() == loginId) {
                loginUser = true;
            }

            // 만약 자신이 다른 사용자의 팔로워를 보는 경우
            if (followRepository.existsByFromUserIdAndToUserId(loginId, follower.getFromUser().getId())) {
                followState = true;
            }

            followData.add(
                    FollowData.builder()
                            .userId(follower.getFromUser().getId())
                            .imageUrl(follower.getFromUser().getImageUrl())
                            .nickname(follower.getFromUser().getNickname())
                            .loginUser(loginUser)
                            .followState(followState)
                            .build()
            );
        }

        if (nickname != null) {
            followData = followData.stream().filter(r -> r.getNickname().equals(nickname)).collect(Collectors.toList());
        }

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), followData.size());
        Page<FollowData> followDataPage = new PageImpl<>(followData.subList(start, end), pageable, followData.size());

        return new FollowResultData(followers.size(), followDataPage);
    }

    public FollowingResultData getFollowing(Long userId, Long loginId, String nickname, Pageable pageable) {

        List<Follow> followers = followRepository.findByFromUserId(userId);
        List<FollowData> followResultData = new ArrayList<>();

        for (Follow follower : followers) {
            boolean loginUser = false;
            boolean followState = false;

            // 만약 로그인한 사용자(자신)이 팔로워 목록에 포함되어 있다면
            // from은 팔로워 to는 팔로우
            if (follower.getToUser().getId() == loginId) {
                loginUser = true;
            }

            // 만약 로그인한 사용자(자신)이 다른 사용자(user)의 팔로워를 보는 경우
            if (followRepository.existsByFromUserIdAndToUserId(loginId, follower.getToUser().getId())) {
                followState = true;
            }

            followResultData.add(
                    FollowData.builder()
                            .userId(follower.getToUser().getId())
                            .imageUrl(follower.getToUser().getImageUrl())
                            .nickname(follower.getToUser().getNickname())
                            .loginUser(loginUser)
                            .followState(followState)
                            .build()
            );
        }
        if (nickname != null) {
            followResultData = followResultData.stream().filter(r -> r.getNickname().equals(nickname)).collect(Collectors.toList());
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), followResultData.size());
        Page<FollowData> followDataPage = new PageImpl<>(followResultData.subList(start, end), pageable, followResultData.size());

        return new FollowingResultData(followers.size(), followDataPage);
    }

}
