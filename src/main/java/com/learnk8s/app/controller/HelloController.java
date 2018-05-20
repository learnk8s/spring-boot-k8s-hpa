package com.learnk8s.app.controller;

import com.learnk8s.app.model.Ticket;
import com.learnk8s.app.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
public class HelloController {

    @Autowired
    private QueueService queueService;

    @Value("${mainQueueName}")
    private String mainQueueName;

    @Value("${workerQueueName}")
    private String workerQueueName;

    @Value("${storeEnabled}")
    private boolean storeEnabled;

    @Value("${workerEnabled}")
    private boolean workerEnabled;

    @GetMapping("/")
    public String home(Model model) {
        Long pendingMessages = queueService.pendingJobs(mainQueueName);
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("pendingJobs", pendingMessages);
        model.addAttribute("completedJobs", queueService.completedJobs());
        model.addAttribute("isConnected", queueService.isUp() ? "yes" : "no");
        model.addAttribute("mainQueue", this.mainQueueName);
        model.addAttribute("workerName", this.workerQueueName);
        model.addAttribute("isStoreEnabled", this.storeEnabled);
        model.addAttribute("isWorkerEnabled", this.workerEnabled);
        return "home";
    }

    @PostMapping("/tickets")
    public String tickets(@ModelAttribute Ticket ticket) {
        for (long i = 0; i < ticket.getQuantity(); i++) {
            String id = UUID.randomUUID().toString();
            queueService.addJob(mainQueueName, id);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value="/metrics", produces="text/plain")
    public String metrics() {
        Long totalMessages = queueService.pendingJobs(mainQueueName);
        return "# HELP messages Number of messages in the queue\n"
                + "# TYPE messages gauge\n"
                + "messages " + totalMessages.toString();
    }

    @RequestMapping(value="/health")
    public ResponseEntity health() {
        HttpStatus status;
        if (queueService.isUp()) {
            status = HttpStatus.OK;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(status);
    }
}