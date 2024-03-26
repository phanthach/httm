package com.httm.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.httm.Service.ScheduledTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ScheduledTaskController {
    @Autowired
    private ScheduledTask scheduledTask;
    @GetMapping("/trigger-task")
    public String triggerScheduledTask() throws JsonProcessingException {
        scheduledTask.distributeData();
        return "Scheduled task triggered!";
    }
}