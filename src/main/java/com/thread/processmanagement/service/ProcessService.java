package com.thread.processmanagement.service;

import com.thread.processmanagement.entity.Data;
import com.thread.processmanagement.exception.DisruptedException;

import java.util.List;

public interface ProcessService {
    List<Data> startProcess();
    List<Data> stopProcess(Long id) throws DisruptedException;
}
