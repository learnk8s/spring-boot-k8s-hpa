package com.learnk8s.app.controller;

import com.learnk8s.app.model.Ticket;
import com.learnk8s.app.queue.QueueService;
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

    @Value("${queue.name}")
    private String queueName;

    @Value("${worker.name}")
    private String workerName;

    @Value("${store.enabled}")
    private boolean storeEnabled;

    @Value("${worker.enabled}")
    private boolean workerEnabled;

    @GetMapping("/")
    public String home(Model model) {
        int pendingMessages = queueService.pendingJobs(queueName);
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("pendingJobs", pendingMessages);
        model.addAttribute("completedJobs", queueService.completedJobs());
        model.addAttribute("isConnected", queueService.isUp() ? "yes" : "no");
        model.addAttribute("queueName", this.queueName);
        model.addAttribute("workerName", this.workerName);
        model.addAttribute("isStoreEnabled", this.storeEnabled);
        model.addAttribute("isWorkerEnabled", this.workerEnabled);
        return "home";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute Ticket ticket) {
        for (long i = 0; i < ticket.getQuantity(); i++) {
            String id = UUID.randomUUID().toString();
            queueService.send(queueName, id);
        }
        return "success";
    }

    @ResponseBody
    @RequestMapping(value="/metrics", produces="text/plain")
    public String metrics() {
        int totalMessages = queueService.pendingJobs(queueName);
        return "# HELP messages Number of messages in the queueService\n"
                + "# TYPE messages gauge\n"
                + "messages " + totalMessages;
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