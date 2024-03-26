package com.httm.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.httm.DAO.*;
import com.httm.Model.Data;
import com.httm.Model.Modelinfo;
import com.httm.Model.Test;
import com.httm.Model.Train;
import com.opencsv.CSVWriter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Log4j2
public class ScheduledTask {
    @Autowired
    private DataRepository dataRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private CheckDataRepository checkDataRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private MachineLearningModelRepository machineLearningModelRepository;

    private double tongdulieucu;
    private double tongdulieumoi;
    private static final String PYTHON_API_URL = "http://localhost:5000/train-model";

    @Autowired
    private PlatformTransactionManager transactionManager;
    @Scheduled(fixedRate = 10000)
    @Transactional
    public void distributeData() throws JsonProcessingException {
        tongdulieucu = (double) checkDataRepository.findSoluong();
        tongdulieumoi = (double)dataRepository.countData();
        Date currentDate = new Date();
        log.info("tongdulieucu = {}", tongdulieucu);
        log.info("tongdulieumoi = {}", tongdulieumoi);
        double phantram= ((tongdulieumoi - tongdulieucu)/tongdulieucu)*100;
        log.info("phantram= {}", phantram);
        if (phantram >= 10) {
            trainRepository.truncateTable();
            testRepository.truncateTable();

            long train = (long) ((tongdulieumoi) * 0.7);

            // Mở một giao dịch mới
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            TransactionStatus status = transactionManager.getTransaction(def);

            try {
                trainRepository.saveDataToTrain((long) 1, train);
                testRepository.saveDataToTest(train + 1, (long) tongdulieumoi);
                checkDataRepository.updateSoluong((long) tongdulieumoi);

                // Commit giao dịch
                transactionManager.commit(status);

                // Gọi API Python sau khi đã commit giao dịch
                String response = restTemplate.getForObject(PYTHON_API_URL, String.class);
                log.info(response);
            } catch (Exception e) {
                // Nếu có lỗi, rollback giao dịch
                transactionManager.rollback(status);
                log.error("Lỗi trong quá trình xử lý dữ liệu", e);
            }
        }
    }
}
