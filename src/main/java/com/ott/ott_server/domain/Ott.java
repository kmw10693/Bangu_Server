package com.ott.ott_server.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ott {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "OTT 식별자 값", example = "1")
    private Long id;

    @ApiModelProperty(value = "OTT 이름", example = "NETFLIX")
    private String name;

}
