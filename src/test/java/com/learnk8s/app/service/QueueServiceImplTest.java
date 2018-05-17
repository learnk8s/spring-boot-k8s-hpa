package com.learnk8s.app.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.learnk8s.app.SpringBootRedisJedispoolApplicationTests;

public class QueueServiceImplTest extends SpringBootRedisJedispoolApplicationTests {

	@Autowired
	private QueueService queueService;
}
