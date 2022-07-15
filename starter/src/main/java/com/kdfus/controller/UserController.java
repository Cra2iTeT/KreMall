package com.kdfus.controller;

import com.alibaba.fastjson.JSON;
import com.kdfus.domain.dto.LoginDTO;
import com.kdfus.domain.vo.Result;
import com.kdfus.util.HeaderUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author Cra2iTeT
 * @date 2022/7/15 23:23
 */

@CrossOrigin
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_REST_URL_PREFIX = "http://user";

    @PostMapping("/login")
    String login(HttpServletRequest request, @RequestBody @Valid LoginDTO loginDTO) {
        HttpHeaders httpHeaders = HeaderUtils.getHeader(request);
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/login",
                HttpMethod.POST,
                new HttpEntity(JSON.toJSONString(loginDTO), httpHeaders),
                Result.class);
        request.setAttribute("authorization", response.getBody().getData());
        return JSON.toJSONString(response.getBody());
    }
}
