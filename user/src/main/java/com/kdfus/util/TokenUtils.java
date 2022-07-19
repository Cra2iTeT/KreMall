package com.kdfus.util;

import com.alibaba.fastjson.JSON;
import com.kdfus.domain.vo.user.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import static com.kdfus.system.RedisConstants.LOGIN_USER_TOKEN_KEY;

/**
 * @author Cra2iTeT
 * @date 2022/7/19 10:09
 */
@Component
public class TokenUtils {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public UserVO verify(String token) {
        String userVOJson = stringRedisTemplate.opsForValue().get(LOGIN_USER_TOKEN_KEY + token);
        UserVO userVO = JSON.parseObject(userVOJson, UserVO.class);
        return userVO;
    }
}
