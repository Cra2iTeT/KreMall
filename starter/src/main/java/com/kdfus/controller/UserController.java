package com.kdfus.controller;

import com.alibaba.fastjson.JSON;
import com.kdfus.domain.dto.LoginDTO;
import com.kdfus.domain.dto.RegistryDTO;
import com.kdfus.domain.dto.UpdatePwdDTO;
import com.kdfus.domain.vo.Result;
import com.kdfus.util.RequestUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

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
    Result<String> login(HttpServletRequest request, @RequestBody @Valid LoginDTO loginDTO) {
        HttpHeaders httpHeaders = RequestUtils.getHeader(request);
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/login",
                HttpMethod.POST,
                new HttpEntity(loginDTO, httpHeaders),
                Result.class);
        return response.getBody();
    }

    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        HttpHeaders httpHeaders = RequestUtils.getHeader(request);
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/logout",
                HttpMethod.POST,
                new HttpEntity(httpHeaders),
                Result.class);
        return response.getBody();
    }

    @PostMapping("/registry")
    public Result<String> registry(@RequestBody @Valid RegistryDTO registryDTO) {
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/logout",
                HttpMethod.POST,
                new HttpEntity(registryDTO),
                Result.class);
        return response.getBody();
    }

    @PutMapping("/update/info")
    public Result<String> update(HttpServletRequest request, String nickName) {
        HttpHeaders httpHeaders = RequestUtils.getHeader(request);
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/update/info",
                HttpMethod.POST,
                new HttpEntity(nickName, httpHeaders),
                Result.class);
        return response.getBody();
    }

    @PutMapping("/update")
    public Result<String> update(HttpServletRequest request, @RequestBody UpdatePwdDTO updatePwdDTO) {
        HttpHeaders httpHeaders = RequestUtils.getHeader(request);
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/update",
                HttpMethod.POST,
                new HttpEntity(updatePwdDTO, httpHeaders),
                Result.class);
        return response.getBody();
    }

    @PutMapping("/update/img")
    public Result<String> update(HttpServletRequest request, MultipartFile file) {
        HttpHeaders httpHeaders = RequestUtils.getHeader(request);
        ResponseEntity<Result> response = restTemplate.exchange(
                USER_REST_URL_PREFIX + "/update/img",
                HttpMethod.POST,
                new HttpEntity(file, httpHeaders),
                Result.class);
        return response.getBody();
    }
}
