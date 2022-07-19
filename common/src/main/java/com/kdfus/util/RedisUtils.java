package com.kdfus.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Cra2iTeT
 * @date 2022/7/19 15:42
 */
@Component
public class RedisUtils {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Set<String> fuzzyMatch(String patternKey) {
        ScanOptions options = ScanOptions.scanOptions()
                .count(10000)
                .match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>)
                stringRedisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) stringRedisTemplate.executeWithStickyConnection
                (redisConnection -> new ConvertingCursor<>
                        (redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(cursor.next().toString());
        }
        cursor.close();
        // 去重
        return new HashSet<>(result);
    }
}
