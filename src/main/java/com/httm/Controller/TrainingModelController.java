package com.httm.Controller;

import com.httm.DAO.DataRepository;
import com.httm.DAO.TestRepository;
import com.httm.DAO.TrainRepository;
import com.httm.Model.Data;
import com.httm.Model.MachineLearningModel;
import com.httm.Model.Test;
import com.httm.Model.Train;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/training_model")
@Transactional
@Log4j2
public class TrainingModelController {
    @Autowired
    public DataRepository dataRepository;
    @Autowired
    public TrainRepository trainRepository;
    @Autowired
    public TestRepository testRepository;
    @Autowired
    private RestTemplate restTemplate;
    private static final String PYTHON_API_URL = "http://localhost:5000/train-model";

    @Autowired
    private PlatformTransactionManager transactionManager;
    @GetMapping("")
    public String listData(Model listdata) {
        List<Data> listData = dataRepository.findAll();
        listdata.addAttribute("listData", listData);
        return "training_model";
    }
    @GetMapping("/search")
    public String searchData(Model Data,
                             @RequestParam("label") String label) {
        List<Data> listData;
        listData = dataRepository.findAllByLabel(label);
        Data.addAttribute("listData", listData);
        Data.addAttribute("label", label);

        return "training_model";
    }
    @PostMapping("/train")
    public ResponseEntity<String> splitData(@RequestBody Map<String, List<Long>> requestBody) {
        // Mở một giao dịch mới
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            trainRepository.truncateTable();
            testRepository.truncateTable();
            List<Long> selectedIds = requestBody.get("selectedIds");

            if (selectedIds == null || selectedIds.isEmpty()) {
                return ResponseEntity.badRequest().body("Không có dữ liệu được chọn.");
            }

            // Chia dữ liệu thành 70% Training Data và 30% Test Data
            int totalSize = selectedIds.size();
            int trainingSize = (int) (totalSize * 0.7);

            // Lưu Training Data vào bảng train_data
            trainRepository.saveDataToTrain(selectedIds.get(0), selectedIds.get(trainingSize - 1));

            // Lưu Test Data vào bảng test_data

            testRepository.saveDataToTest(selectedIds.get(trainingSize), selectedIds.get(totalSize - 1));

            // Commit giao dịch
            transactionManager.commit(status);

            // Gọi API Python sau khi đã commit giao dịch
            String response = restTemplate.getForObject(PYTHON_API_URL, String.class);
            log.info(response);
            return ResponseEntity.ok("Dữ liệu đã được chia thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi khi chia dữ liệu: " + e.getMessage());
        }
    }

}
