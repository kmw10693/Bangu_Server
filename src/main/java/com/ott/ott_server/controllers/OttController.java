package com.ott.ott_server.controllers;

import com.ott.ott_server.application.OttService;
import com.ott.ott_server.domain.Ott;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "OTT 저장 API")
@RestController
@RequestMapping("/otts")
@RequiredArgsConstructor
public class OttController {

    private final OttService ottService;

    @PreAuthorize("permitAll()")
    @PostMapping
    @ApiOperation(value = "OTT 저장", notes = "OTT 정보를 받아 OTT 작성합니다",
            response = Ott.class)
    @ResponseStatus(HttpStatus.CREATED)
    public Ott save(@RequestBody Ott ott) {
        return ottService.save(ott);
    }

}
