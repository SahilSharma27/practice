package com.sahil.ecom.service.impl;

import com.sahil.ecom.RIUtilizationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TemplateService {

    @Autowired
    EmailSenderService emailSenderService;

    public void sendEmailTemplate() throws MessagingException {
        List<RIUtilizationDto> riUtilizationList = new ArrayList<>();
        RIUtilizationDto riUtilizationDto1 =  new RIUtilizationDto();

//        String reservation;
        riUtilizationDto1.setReservation("arn:aws:ec2:us-west-2:reserved-instances/");
//        String region;
        riUtilizationDto1.setRegion("US");
//        String operatingSystem;
        riUtilizationDto1.setOperatingSystem("Linux/UNIX");
//        String instanceType;
        riUtilizationDto1.setInstanceType("m5.4xlarge");
//        double reservedHours;
        riUtilizationDto1.setReservedHours(24.0);
//        double usedHours;
        riUtilizationDto1.setUsedHours(23.0);
//        double unusedHours;
//        riUtilizationDto1.setUnusedHours(0.0);
//        double quantity;
        riUtilizationDto1.setQuantity(1.0);
//        double netSavings;
        riUtilizationDto1.setNetSavings(10.862399894714358);
//        Double usedPercentage;
        riUtilizationDto1.setUsedPercentage(100.0);
//        String productName;
        riUtilizationDto1.setProductName("Amazon Elastic Compute Cloud");
//        Double onDemandCost;

//        Integer multiAzVal;




        RIUtilizationDto riUtilizationDto2=  new RIUtilizationDto();
        riUtilizationDto2.setInstanceType("TEST");
        riUtilizationDto2.setInstanceType("TEST");
        riUtilizationDto2.setNetSavings(1000);
        riUtilizationDto2.setRegion("TEST");

        riUtilizationList.add(riUtilizationDto1);
        riUtilizationList.add(riUtilizationDto2);

        emailSenderService.sendHtmlMessage(riUtilizationList);
    }

}
