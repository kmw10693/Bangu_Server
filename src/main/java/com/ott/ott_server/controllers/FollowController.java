package com.ott.ott_server.controllers;

import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.infra.FollowRepository;
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
    public ResponseEntity<?> followUser(@PathVariable("toUser") Long toUserId, Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        User toUser = userService.getUser(toUserId);
        followService.follow(user, toUser);
        return new ResponseEntity<>("팔로우 성공", HttpStatus.OK);
    }

    @DeleteMapping("/{toUserId}")
    public ResponseEntity<?> unFollowUser(@PathVariable Long toUserId, Authentication authentication) {
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());
        User toUser = userService.getUser(toUserId);
        followService.unFollow(user, toUser);
        return new ResponseEntity<>("팔로우 취소 성공", HttpStatus.OK);
    }

}
