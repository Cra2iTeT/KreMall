package com.kdfus;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Cra2iTeT
 * @date 2022/7/19 14:48
 */
@SpringBootTest
@Slf4j
public class TestApp {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void test() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 2146227; i++) {
            stringRedisTemplate.delete("test:2022-7-19:" + i);
        }
        log.info("添加数据共耗时：{} ms", System.currentTimeMillis() - start);
    }

    @Test
    void test3() {
        long start = System.currentTimeMillis();
        Set<String> keys = stringRedisTemplate.keys("test:2022-7-19:*");
        log.info("keys扫描共耗时：{} ms key数量：{}", System.currentTimeMillis() - start, keys.size());
    }

    @Test
    void test2() {
        long start = System.currentTimeMillis();
        String patternKey = "test:2022-7-19:*";
        ScanOptions options = ScanOptions.scanOptions()
                .count(10000)
                .match(patternKey).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) stringRedisTemplate.getKeySerializer();
        Cursor cursor = (Cursor) stringRedisTemplate.executeWithStickyConnection(redisConnection -> new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize));
        List<String> result = new ArrayList<>();
        while (cursor.hasNext()) {
            result.add(cursor.next().toString());
        }
        cursor.close();
        log.info("scan扫描共耗时：{} ms key数量：{}", System.currentTimeMillis() - start, result.size());
    }
}
