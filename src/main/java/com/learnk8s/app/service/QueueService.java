package com.learnk8s.app.service;

public interface QueueService {

	boolean addJob(String list, String val);

    void processJobs(String mainQueueName, String workerQueueName);

	Long pendingJobs(String mainQueueName);

	boolean isUp();

	Long completedJobs();
}

