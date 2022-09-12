package com.ott.ott_server.controllers;

import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Api(tags = "팔로우 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
public class FollowController {

    private final FollowService followService;
    private final UserService userService;

    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @PostMapping("/{toUser}")
    @ApiOperation(value = "팔로우 기능", notes = "유저 Id를 받아 해당 유저를 팔로우 합니다.")
    public ResponseEntity<?> followUser(@PathVariable("toUser") @ApiParam(value = "유저 식별자 값") Long toUserId) {
        User toUser = userService.getUser(toUserId);
        followService.follow(toUser);
        return new ResponseEntity<>("팔로우 성공", HttpStatus.OK);
    }

    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @DeleteMapping("/{toUserId}")
    @ApiOperation(value = "언팔로우 기능", notes = "유저 id를 받아 해당 유저를 언팔로우 합니다.")
    public ResponseEntity<?> unFollowUser(@PathVariable @ApiParam(value = "유저 식별자 값") Long toUserId) {
        User toUser = userService.getUser(toUserId);
        followService.unFollow(toUser);
        return new ResponseEntity<>("팔로우 취소 성공", HttpStatus.OK);
    }

}
