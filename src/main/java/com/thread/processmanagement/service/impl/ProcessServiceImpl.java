package com.thread.processmanagement.service.impl;

import com.thread.processmanagement.entity.Data;
import com.thread.processmanagement.exception.DisruptedException;
import com.thread.processmanagement.service.ProcessService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ProcessServiceImpl implements ProcessService {

    List<Data> dbStore = new ArrayList<Data>();  //At Any Time db will have single data.

    private final int GLOBAL_PROCESSING_THRESHOLD_MILLISECONDS = 3000;   //3 Seconds

    @Override
    public List<Data> startProcess() {
        invalidatePreviousRequest();
        addNewRequest();
        return dbStore;
    }

    @Override
    public List<Data> stopProcess(Long id) throws DisruptedException {
        boolean isValidProcessId = isValidProcessId(id);
        if (isValidProcessId) {
            invalidateRequests(id);
        } else {
            throw new DisruptedException("Given Data Id is Already Disrupted by previous requests");
        }
        return dbStore;
    }

    void invalidateRequests(Long id) {
        for (int i = 0; i < dbStore.size(); i++) {
            Data data = dbStore.get(i);
            if (data.getId() == id) {
                dbStore.remove(data);
            }
        }
    }

    boolean isValidProcessId(Long id) {
        for (int i = 0; i < dbStore.size(); i++) {
            Data data = dbStore.get(i);
            if (data.getId() == id) {
                return true;
            }
        }
        return false;
    }

    void addNewRequest() {
        Data data = new Data();
        data.setData("This is our data");
        data.setDate(new Date());
        Long id = (long)dbStore.size()+1;
        data.setId(id);
        dbStore.add(data);
    }

     void invalidatePreviousRequest() {
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
    }

}
