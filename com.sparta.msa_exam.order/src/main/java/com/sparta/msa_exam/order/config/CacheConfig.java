package com.sparta.msa_exam.order.config;

import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory){
        // 설정 구성을 먼저 진행한다.
        // redis 를 이용해서 Spring cache 를 사용할 때
        // redis 관련 설정을 모아두는 클래스
        RedisCacheConfiguration configuration = RedisCacheConfiguration
            .defaultCacheConfig()
            // null 을 캐싱하는지
            .disableCachingNullValues()
            // Time to Live
            .entryTtl(Duration.ofSeconds(60))
            // 캐시를 구분하는 접두사 설정
            .computePrefixWith(CacheKeyPrefix.simple())
            // 캐시에 저장할 값을 어떻게 직렬화 / 역직렬화 할것인지
            // java byte 코드로 redis 에 값을 저장, 읽는데 어려움이 있다.

            .serializeValuesWith(
                SerializationPair.fromSerializer(RedisSerializer.json())
            );

        return RedisCacheManager
            .builder(redisConnectionFactory)
            .cacheDefaults(configuration)
            // key 에 따른 string, json, java 를 구별하기 위한 설정
//            .withInitialCacheConfigurations()
            .build();
    }
}
