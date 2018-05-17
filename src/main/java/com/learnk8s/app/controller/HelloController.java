package com.learnk8s.app.controller;

import com.learnk8s.app.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RestController
public class HelloController {

    @Autowired
    private QueueService queueService;

    @Value("${mainQueueName}")
    private String mainQueueName;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/submit")
    public String submit() {
        String id = UUID.randomUUID().toString();
        queueService.addJob(mainQueueName, id);
        return id;
    }

    @RequestMapping(value="/metrics", produces="text/plain")
    public String metrics() {
        Long totalMessages = queueService.pendingJobs(mainQueueName);
        return "# HELP messages Number of messages in the queue\n"
                + "# TYPE messages gauge\n"
                + "messages " + totalMessages.toString();
    }
}