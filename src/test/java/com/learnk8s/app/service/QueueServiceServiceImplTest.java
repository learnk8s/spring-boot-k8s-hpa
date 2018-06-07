package com.learnk8s.app.service;

import com.learnk8s.app.queue.QueueService;
import org.springframework.beans.factory.annotation.Autowired;

import com.learnk8s.app.SpringBootApplicationTests;

public class QueueServiceServiceImplTest extends SpringBootApplicationTests {

	@Autowired
	private QueueService queueService;
}
