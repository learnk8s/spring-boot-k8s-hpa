package com.learnk8s.app.config;

import com.learnk8s.app.service.QueueServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceImpl.class);

	@Bean
	public JedisPool jedisPool(@Value("${redisUrl}") String redisUrl) {
	    LOGGER.info("Using REDIS_URL=" + redisUrl);
		return new JedisPool(redisUrl);
	}
}
