package com.learnk8s.app.service;

import com.learnk8s.app.queue.QueueService;
import org.springframework.beans.factory.annotation.Autowired;

import com.learnk8s.app.SpringBootRedisJedispoolApplicationTests;

public class QueueServiceServiceImplTest extends SpringBootRedisJedispoolApplicationTests {

	@Autowired
	private QueueService queueService;
}
