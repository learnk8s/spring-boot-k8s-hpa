package com.learnk8s.app;

import com.learnk8s.app.service.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class SpringBootRedisJedispoolApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootRedisJedispoolApplication.class);

	@Autowired
	private QueueService queueService;

    @Value("${mainQueueName}")
    private String mainQueueName;


    @Value("${workerQueueName}")
    private String workerQueueName;

    @Value("${workerEnabled}")
    private boolean workerEnabled;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootRedisJedispoolApplication.class, args);
	}

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.info("Using MAIN_QUEUE=" + mainQueueName);

        if (workerEnabled) {
            LOGGER.info("Using WORKER_QUEUE=" + workerQueueName);
            queueService.processJobs(mainQueueName, workerQueueName);
        }
    }
}
