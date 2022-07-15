package com.kdfus.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Cra2iTeT
 * @version 1.0
 * @date 2022/4/13 15:07
 */
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //  没有用户登录
        if (request.getMethod().toUpperCase().equals("OPTIONS")) {
            //通过所有OPTION请求
            return true;
        }
        if (request.getHeader("authorization") == null) {
            response.setStatus(401);
            return false;
        }
        return true;
    }
}