package com.ott.ott_server.controllers;

import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.infra.FollowRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;
    private final UserService userService;

    @PostMapping("/{toUser}")
    @ApiOperation(value = "팔로우 기능", notes = "유저 idx를 받아 해당 유저를 팔로우합니다." +
            "헤더에 사용자 토큰 주입을 필요로 합니다.")
    public ResponseEntity<?> followUser(@PathVariable("toUser") @ApiParam(value = "유저 식별자 값") Long toUserId, Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        User toUser = userService.getUser(toUserId);
        followService.follow(user, toUser);
        return new ResponseEntity<>("팔로우 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{toUserId}")
    @ApiOperation(value = "언팔로우 기능", notes = "유저 idx를 받아 해당 유저를 언팔로우합니다." +
            "헤더에 사용자 토큰 주입을 필요로 합니다.")
    public ResponseEntity<?> unFollowUser(@PathVariable @ApiParam(value = "유저 식별자 값") Long toUserId, Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        User toUser = userService.getUser(toUserId);
        followService.unFollow(user, toUser);
        return new ResponseEntity<>("팔로우 취소 성공", HttpStatus.OK);
    }

}
