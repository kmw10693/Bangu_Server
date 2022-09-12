package com.ott.ott_server.controllers;

import com.ott.ott_server.application.CustomUserDetailsService;
import com.ott.ott_server.application.FollowService;
import com.ott.ott_server.application.UserService;
import com.ott.ott_server.config.SecurityConfig;
import com.ott.ott_server.domain.User;
import com.ott.ott_server.domain.enums.Gender;
import com.ott.ott_server.errors.UserEmailDuplicationException;
import com.ott.ott_server.errors.UserNickNameDuplicationException;
import com.ott.ott_server.filter.JwtAuthenticationFilter;
import com.ott.ott_server.provider.JwtProvider;
import com.ott.ott_server.utils.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
class UserControllerTest {

    @MockBean
    private UserUtil userUtil;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private FollowService followService;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setup() {
        User user = User.builder()
                .id(1L)
                .email("abc123")
                .password("abc1234")
                .nickname("반구")
                .birth(2001L)
                .gender(Gender.M)
                .imageUrl("https://dullyshin.github.io/2018/08/30/HTML-imgLink/")
                .build();

        given(customUserDetailsService.loadUserByUsername(anyString())).willReturn(user);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("test", "1234", Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
        given(jwtProvider.getAuthentication(anyString())).willReturn(authentication);
    }

    @Test
    @DisplayName("존재하는_아이디를_조회하는_경우")
    void confirmEmail() throws Exception {

        given(userService.isDuplicateEmail("abc123"))
                .willThrow(new UserEmailDuplicationException("abc123"));

        mockMvc.perform(
                        get("/users/emailCheck/abc123")
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(userService).isDuplicateEmail("abc123");
    }

    @Test
    @DisplayName("존재하지_않은_아이디를_확인하려는_경우")
    void confirmInvalidEmail() throws Exception {

        given(userService.isDuplicateEmail("khj10693"))
                .willReturn(false);

        mockMvc.perform(
                        get("/users/emailCheck/khj10693")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("false")
                ))
                .andDo(print());
        verify(userService).isDuplicateEmail("khj10693");
    }

    @Test
    @DisplayName("존재하는_닉네임를_조회하는_경우")
    void confirmNickname() throws Exception {

        given(userService.isDuplicateNickname("bangu"))
                .willThrow(new UserNickNameDuplicationException("bangu"));

        mockMvc.perform(
                        get("/users/nicknameCheck/bangu")
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
        verify(userService).isDuplicateNickname("bangu");
    }

    @Test
    @DisplayName("존재하지_않는_닉네임를_조회하는_경우")
    void confirmInvalidNickname() throws Exception {

        given(userService.isDuplicateNickname("bangu"))
                .willReturn(false);

        mockMvc.perform(
                        get("/users/nicknameCheck/bangu")
                )
                .andExpect(status().isOk())
                .andExpect(content().string(
                        containsString("false")
                ))
                .andDo(print());
        verify(userService).isDuplicateNickname("bangu");
    }

}
