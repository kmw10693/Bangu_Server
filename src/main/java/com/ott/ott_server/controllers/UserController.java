package com.ott.ott_server.controllers;

import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.follow.FollowResultData;
import com.ott.ott_server.dto.user.UserModificationData;
import com.ott.ott_server.dto.user.UserResultData;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;

    /**
     * 아이디 중복 확인 API
     * [GET] /users/emailCheck/:userEmail
     */
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "아이디 중복확인",
            notes = "DB에 입력된 아이디의 존재 여부를 리턴합니다. 존재하면 true, 존재하지 않으면 false를 반환합니다.")
    @ApiImplicitParam(name = "userEmail", dataType = "string", value = "사용자 아이디")
    @GetMapping("/emailCheck/{userEmail}")
    public ResponseEntity<Boolean> checkEmail(@PathVariable String userEmail) {
        return ResponseEntity.ok(userService.isDuplicateEmail(userEmail));
    }

    /**
     * 닉네임 중복 확인 API
     * [GET] /users/nicknameCheck/:nickname
     */
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "닉네임 중복확인",
            notes = "DB에 입력된 닉네임의 존재 여부를 리턴합니다. 존재하면 true, 존재하지 않으면 false를 반환합니다.")
    @ApiImplicitParam(name = "nickname", dataType = "string", value = "사용자 닉네임")
    @GetMapping("/nicknameCheck/{nickname}")
    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
        return ResponseEntity.ok(userService.isDuplicateNickname(nickname));
    }

    /**
     * 사용자 정보 조회 API
     * [GET] /users
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @ApiOperation(value = "현재 사용자 조회", notes = "발급받은 토큰을 통해 현재 사용자의 정보를 조회합니다. " +
            "헤더에 사용자 토큰 주입을 필요로 합니다.",
            response = UserResultData.class)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserResultData detail(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userService.getUser(userDetails.getUsername());
        return user.toUserResultData();
    }

    /**
     * 사용자 업데이트 API
     * [PATCH] /users/:id
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @PatchMapping("/{id}")
    @ApiOperation(value = "사용자 업데이트",
            notes = "전달받은 사용자의 식별자로 수정할 사용자를 찾아, 주어진 데이터로 사용자의 정보를 갱신합니다." +
                    "헤더에 사용자 토큰 주입을 필요로 합니다.",
            response = UserResultData.class)
    @ResponseStatus(HttpStatus.OK)
    @ApiImplicitParam(name = "id", dataType = "integer", value = "사용자 식별자")
    public UserResultData update(@PathVariable("id") Long id, @RequestBody @Valid UserModificationData modificationData,
                                 Authentication authentication) throws AccessDeniedException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        User user = userService.updateUser(id, modificationData, email);
        return user.toUserResultData();
    }

    /**
     * 사용자 삭제 API
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @DeleteMapping("/{id}")
    @ApiOperation(value = "사용자 삭제",
            notes = "전달받은 사용자의 식별자로 삭제할 사용자를 찾아, 주어진 데이터로 사용자의 정보를 삭제합니다.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiImplicitParam(name = "id", dataType = "integer", value = "사용자 식별자")
    public void destroy(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    /**
     * 유저 팔로워 조회 API
     *
     * @param id
     * @param authentication
     * @return
     * @throws AccessDeniedException
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{id}/follower")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "유저 팔로워 조회",
            notes = "전달받은 사용자의 식별자로 사용자의 팔로워를 조회합니다.")
    @ApiImplicitParam(name = "id", dataType = "integer", value = "사용자 식별자")
    public List<FollowResultData> getFollower(@PathVariable Long id,
                                              Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());

        return followService.getFollower(id, user.getId());
    }

    /**
     * 유저 팔로잉 조회 API
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header")
    })
    @GetMapping("/{id}/following")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "유저 팔로잉 조회",
            notes = "전달받은 사용자의 식별자로 사용자의 팔로잉을 조회합니다.")
    @ApiImplicitParam(name = "id", dataType = "integer", value = "사용자 식별자")
    public List<FollowResultData> getFollowing(@PathVariable Long id,
                                               Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userService.getUser(userDetails.getUsername());

        return followService.getFollowing(id, user.getId());
    }

}
