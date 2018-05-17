package com.learnk8s.app.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	@Bean
	public JedisPool jedisPool(@Value("${redisUrl}") String redisUrl) {
		return new JedisPool(redisUrl);
	}
}
