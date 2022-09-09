package com.ott.ott_server.controllers;

import com.ott.ott_server.application.OttService;
import com.ott.ott_server.domain.Ott;
import com.ott.ott_server.dto.ott.request.OttRequestData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Api(tags = "OTT 저장 API")
@RestController
@RequestMapping("/otts")
@RequiredArgsConstructor
public class OttController {

    private final OttService ottService;

    @PostMapping
    @ApiOperation(value = "OTT 목록 저장", notes = "OTT 이름을 받아 OTT 목록을 저장하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTT 목록이 정상적으로 저장된 경우"),
            @ApiResponse(responseCode = "400", description = "OTT 목록 저장을 실패한 경우")
    })
    public ResponseEntity<Ott> create(@RequestBody OttRequestData ottRequestData) {
        return ResponseEntity.ok(ottService.create(ottRequestData));
    }

    @GetMapping
    @ApiOperation(value = "OTT 목록 조회", notes = "DB의 OTT 목록을 조회하는 API")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OTT 목록이 정상적으로 조회된 경우"),
            @ApiResponse(responseCode = "400", description = "OTT 목록 조회를 실패한 경우")
    })
    public ResponseEntity<Page<Ott>> getAllOtts(Pageable pageable) {
        return ResponseEntity.ok(ottService.getAllOtts(pageable));
    }

}
