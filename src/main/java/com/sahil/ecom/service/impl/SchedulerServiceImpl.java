package com.sahil.ecom.service.impl;

import com.sahil.ecom.service.SchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    Logger logger= LoggerFactory.getLogger(SchedulerServiceImpl.class);

    @Autowired
    private EmailSenderService emailSenderService;

//    @Scheduled(cron = "*/10 * * * * *")
//    @Scheduled(cron = "@midnight")
    public void sendMail(){
        logger.info("_________________JOB DONE________________");
    }
}
