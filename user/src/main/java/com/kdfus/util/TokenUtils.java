package com.kdfus.util;

import com.alibaba.fastjson.JSON;
import com.kdfus.domain.vo.user.UserVO;
import org.springframework.data.redis.core.StringRedisTemplate;

import static com.kdfus.system.RedisConstants.LOGIN_USER_TOKEN_KEY;

/**
 * @author Cra2iTeT
 * @date 2022/7/19 10:09
 */
public class TokenUtils {

    public static UserVO verify(String token, StringRedisTemplate stringRedisTemplate) {
        String userVOJson = stringRedisTemplate.opsForValue().get(LOGIN_USER_TOKEN_KEY + token);
        UserVO userVO = JSON.parseObject(userVOJson, UserVO.class);
        return userVO;
    }
}
