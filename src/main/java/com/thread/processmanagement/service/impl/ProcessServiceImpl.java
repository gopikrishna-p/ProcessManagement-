package com.thread.processmanagement.service.impl;

import com.thread.processmanagement.entity.Data;
import com.thread.processmanagement.exception.DisruptedException;
import com.thread.processmanagement.service.ProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ProcessServiceImpl implements ProcessService {

    private static final String LOGGER_TAG = "[ProcessServiceImpl]";
    private static final Logger logger = LoggerFactory.getLogger(ProcessServiceImpl.class);

    List<Data> dbStore = new ArrayList<Data>();  //At Any Time db will have single data.

    private final int GLOBAL_PROCESSING_THRESHOLD_MILLISECONDS = 3000;   //3 Seconds

    @Override
    public List<Data> startProcess() {
        logger.info("Inside startProcess method.");
        invalidatePreviousRequest();
        addNewRequest();
        logger.info("Exit startProcess method.");
        return dbStore;
    }

    @Override
    public List<Data> stopProcess(Long id) throws DisruptedException {
        logger.info("Inside stopProcess method.");
        boolean isValidProcessId = isValidProcessId(id);
        if (isValidProcessId) {
            invalidateRequests(id);
        } else {
            throw new DisruptedException("Given Data Id is Already Disrupted by previous requests");
        }
        logger.info("Exit stopProcess method.");
        return dbStore;
    }

    void invalidateRequests(Long id) {
        logger.info("Inside Invalidate Requests method.");
        for (int i = 0; i < dbStore.size(); i++) {
            Data data = dbStore.get(i);
            if (data.getId() == id) {
                dbStore.remove(data);
            }
        }
        logger.info("Exit Invalidate Requests method.");
    }

    boolean isValidProcessId(Long id) {
        logger.info("Inside isValidProcessId method.");
        for (int i = 0; i < dbStore.size(); i++) {
            Data data = dbStore.get(i);
            if (data.getId() == id) {
                return true;
            }
        }
        logger.info("Exit isValidProcessId method.");
        return false;
    }

    void addNewRequest() {
        logger.info("Inside add new request method.");
        Data data = new Data();
        data.setData("This is our data");
        data.setDate(new Date());
        Long id = (long)dbStore.size()+1;
        data.setId(id);
        logger.info("Exit add new request method.");
        dbStore.add(data);
    }

     void invalidatePreviousRequest() {
         logger.info("Inside invalidatePreviousRequest method.");
        if (dbStore.size() > 0) {
            Date currentDate = new Date();
            Long timeInMilliSeconds = currentDate.getTime();
            for (int i=0;i<dbStore.size();i++) {
                Data data = dbStore.get(i);
                Long dbTimeInMilliSeconds = data.getDate().getTime();
                if (timeInMilliSeconds - dbTimeInMilliSeconds < GLOBAL_PROCESSING_THRESHOLD_MILLISECONDS) {  //Within 3 seconds we received another request;
                    //Remove the previous request
                    dbStore.remove(data);
                }
            }
        }
         logger.info("Exit invalidatePreviousRequest method.");
    }

}
