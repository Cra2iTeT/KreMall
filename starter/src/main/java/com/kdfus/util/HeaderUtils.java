package com.kdfus.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/5/21 10:41
 */
@Slf4j
public class HeaderUtils {
    public static HttpHeaders getHeader(HttpServletRequest request) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("authorization",request.getHeader("authorization"));
        httpHeaders.setContentType(MediaType.valueOf("application/json;charset=UTF-8"));
        return httpHeaders;
    }
}
