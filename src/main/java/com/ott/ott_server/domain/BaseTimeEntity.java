package com.ott.ott_server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "생성 시각", example = "")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(updatable = false)
    @ApiModelProperty(value = "업데이트 시각", example = "")
    private LocalDateTime updateAt;

}
