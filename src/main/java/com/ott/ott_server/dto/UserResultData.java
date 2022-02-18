package com.ott.ott_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class UserResultData {
    private Long id;

    private String email;

    private String nickname;

    private LocalDateTime create_at;

    private LocalDateTime update_at;
}
