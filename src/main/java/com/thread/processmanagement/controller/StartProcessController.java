package com.thread.processmanagement.controller;

import com.thread.processmanagement.entity.Data;
import com.thread.processmanagement.exception.DisruptedException;
import com.thread.processmanagement.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String LOGGER_TAG = "[StartProcessController]";
    private static final Logger logger = LoggerFactory.getLogger(StartProcessController.class);

    @Autowired
    ProcessService processService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    ResponseEntity<Object> start() {
        List<Data> dbStore = processService.startProcess();
        logger.info("Inside start method.");
        return new ResponseEntity<>(dbStore, HttpStatus.OK);
    }

    @RequestMapping(value = "/stop/{id}", method = RequestMethod.POST)
    ResponseEntity<Object> stop(@PathVariable Long id) throws DisruptedException {
        List<Data> dbStore = processService.stopProcess(id);
        logger.info("Inside Stop Method.");
        return new ResponseEntity<>(dbStore, HttpStatus.OK);
    }
}
