package com.learnk8s.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

@Service
public class QueueServiceImpl implements QueueService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueServiceImpl.class);
    private long completedJobs = 0;

	@Autowired
	private JedisPool jedisPool;

	@Override
	public boolean addJob(String listName, String val) {
		Jedis jedis = this.jedisPool.getResource();
		try {
			return "OK".equals(jedis.lpush(listName, val));
		} finally {
			if (jedis != null)
				jedis.close();
		}
	}

	@Async
	public void processJobs(String mainQueueName, String workerQueueName) {
        Jedis jedis = this.jedisPool.getResource();
        try {
            while (true) {
                List<String> taskIds = jedis.lrange(workerQueueName, 0, 0);
                String taskId = taskIds.size() > 0 ? taskIds.get(0) : jedis.brpoplpush(mainQueueName, workerQueueName, 0);
                LOGGER.info("Processing task " + taskId);
                Thread.sleep(5000);
                LOGGER.info("Completed task " + taskId);
                jedis.lrem(workerQueueName, -1, taskId);
                this.completedJobs += 1;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null)
                jedis.close();
        }
	}

	@Override
	public Long pendingJobs(String mainQueueName) {
		Jedis jedis = this.jedisPool.getResource();
		Long ret;
		try {
			ret = jedis.llen(mainQueueName);
		} finally {
			if (jedis != null)
				jedis.close();
		}
		return ret;
	}

	@Override
	public boolean isUp() {
		boolean ret = false;
		try {
            Jedis jedis = this.jedisPool.getResource();
            if (jedis != null)
                jedis.close();
            ret = true;
		} catch (JedisConnectionException e) {
            LOGGER.error("Couldn't connect to Redis");
        }
		return ret;
	}

    @Override
    public Long completedJobs() {
        return this.completedJobs;
    }
}
