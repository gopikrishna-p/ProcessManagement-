package com.thread.processmanagement.controller;

import com.thread.processmanagement.entity.Data;
import com.thread.processmanagement.exception.DisruptedException;
import com.thread.processmanagement.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/process-management/")
public class StartProcessController {

    @Autowired
    ProcessService processService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    ResponseEntity<Object> start() {
        List<Data> dbStore = processService.startProcess();
        return new ResponseEntity<>(dbStore, HttpStatus.OK);
    }

    @RequestMapping(value = "/stop/{id}", method = RequestMethod.POST)
    ResponseEntity<Object> stop(@PathVariable Long id) throws DisruptedException {
        List<Data> dbStore = processService.stopProcess(id);
        return new ResponseEntity<>(dbStore, HttpStatus.OK);
    }
}
