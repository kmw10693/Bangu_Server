package com.ott.ott_server.controllers;

import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.dto.follow.response.FollowResultData;
import com.ott.ott_server.dto.follow.response.FollowingResultData;
import com.ott.ott_server.dto.user.UserPasswordModifyData;
import com.ott.ott_server.dto.user.UserResultData;
import com.ott.ott_server.utils.UserUtil;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "유저 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final FollowService followService;
    private final UserUtil userUtil;


    /**
     * 아이디 중복 확인 API
     * [GET] /users/emailCheck/:userEmail
     */
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
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @ApiOperation(value = "현재 사용자 조회", notes = "발급받은 토큰을 통해 현재 사용자의 정보를 조회합니다.")
    @GetMapping
    @ApiResponse(responseCode = "200", description = "사용자를 정상적으로 조회한 경우")
    public UserResultData detail() {
        User user = userUtil.findCurrentUser();
        return user.toUserResultData();
    }

    /**
     * 비밀번호 업데이트
     */
    // 비밀번호가 유출될 수 있으니 void로 설정
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @PatchMapping("/update/password")
    @ApiOperation(value = "비밀번호 변경", notes = "유저의 패스워드를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "사용자의 비밀번호를 정상적으로 수정한 경우")
    public void updatePassword(UserPasswordModifyData userPasswordModifyData) {
        User user = userUtil.findCurrentUser();
        userService.updatePassword(user, userPasswordModifyData);
    }

    /**
     * 닉네임 업데이트
     */
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @PatchMapping("/update/nickname")
    @ApiOperation(value = "닉네임 변경", notes = "유저의 닉네임을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "사용자의 닉네임을 정상적으로 수정한 경우")
    public void updateNickname(@RequestParam @ApiParam(value = "수정할 닉네임", example = "반구") String nickname) {
        User user = userUtil.findCurrentUser();
        userService.updateNickname(user, nickname);
    }

    /**
     * 프로필 이미지 업데이트
     */
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @PatchMapping("/update/profile")
    @ApiOperation(value = "프로필 사진 변경", notes = "유저의 프로필 사진을 변경합니다.")
    @ApiResponse(responseCode = "200", description = "사용자의 프로필 사진을 정상적으로 수정한 경우")
    public void updateProfile(@RequestParam @ApiParam(value = "수정할 이미지", example = "image.com") String imageUrl) {
        User user = userUtil.findCurrentUser();
        userService.updateImageUrl(user, imageUrl);
    }

    /**
     * 사용자 삭제 API
     */
    @ApiImplicitParam(
            name = "X-AUTH-TOKEN",
            value = "로그인 성공 후 AccessToken",
            required = true, dataType = "String", paramType = "header")
    @DeleteMapping
    @ApiOperation(value = "사용자 삭제",
            notes = "현재 사용자의 정보를 삭제합니다.")
    @ApiResponse(responseCode = "200", description = "사용자의 프로필 사진을 정상적으로 삭제한 경우")
    public void destroy() {
        User user = userUtil.findCurrentUser();
        userService.deleteUser(user);
    }

    /**
     * 유저 팔로워 조회 API
     *
     * @param id
     */
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", dataType = "integer", value = "사용자 식별자")
    })
    @GetMapping("/{id}/follower")
    @ApiOperation(value = "유저 팔로워 조회", notes = "전달받은 사용자의 식별자로 사용자의 팔로워를 조회합니다.")
    public FollowResultData getFollower(@PathVariable Long id, @ApiParam(value = "검색할 닉네임", example = "반구") @RequestParam(required = false) String nickname,
                                        Pageable pageable) {
        User user = userUtil.findCurrentUser();
        return followService.getFollower(id, user.getId(), nickname, pageable);
    }

    /**
     * 유저 팔로잉 조회 API
     */
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "X-AUTH-TOKEN",
                    value = "로그인 성공 후 AccessToken",
                    required = true, dataType = "String", paramType = "header"),
            @ApiImplicitParam(name = "id", dataType = "integer", value = "사용자 식별자")
    })
    @GetMapping("/{id}/following")
    @ApiOperation(value = "유저 팔로잉 조회", notes = "전달받은 사용자의 식별자로 사용자의 팔로잉을 조회합니다.")
    public FollowingResultData getFollowing(@PathVariable Long id, @ApiParam(value = "검색할 닉네임", example = "반구") @RequestParam(required = false) String nickname
            , Pageable pageable) {
        User user = userUtil.findCurrentUser();
        return followService.getFollowing(id, user.getId(), nickname, pageable);
    }

}
