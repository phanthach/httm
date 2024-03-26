package com.httm.Controller;

import com.httm.DAO.LearningHistoryModelRepository;
import com.httm.DAO.MachineLearningModelRepository;
import com.httm.Model.LearningHistoryModel;
import com.httm.Model.MachineLearningModel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.time.DateUtils.parseDate;

@Controller
@RequestMapping("/huan_luyen_model")
@Log4j2
@Transactional
public class huanluyenmodelController {
    @Autowired
    public MachineLearningModelRepository machineLearningModelRepository;
    @Autowired
    public LearningHistoryModelRepository learningHistoryModelRepository;
    @GetMapping("")
    public String listModelMachine(Model listmodel){
        List<MachineLearningModel> listModel = machineLearningModelRepository.findAll();
        listmodel.addAttribute("listModel", listModel);
        return "huan_luyen_model";
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningModel(@PathVariable String id) {
        Optional<MachineLearningModel> model = machineLearningModelRepository.findById(id);
        if (model.isPresent()) {
            machineLearningModelRepository.delete(model.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save-to-history")
    public ResponseEntity<String> saveToHistory(@RequestParam String machineLearningId) {
        MachineLearningModel machineLearningModel = machineLearningModelRepository.findById(machineLearningId).orElse(null);


        if (machineLearningModel != null) {
            LearningHistoryModel learningHistoryModel = new LearningHistoryModel();
            log.info("machineLearningModel123456={}",machineLearningModel.getName_Model());
            learningHistoryModel.setName_Model(machineLearningModel.getName_Model());
            learningHistoryModel.setModel_Path(machineLearningModel.getModel_Path());
            learningHistoryModel.setTraining_Time(machineLearningModel.getTraining_Time());
            learningHistoryModel.setAccuracy(machineLearningModel.getAccuracy());


            learningHistoryModelRepository.insertLearningHistoryModel(machineLearningModel.getName_Model(), machineLearningModel.getTraining_Time(), machineLearningModel.getModel_Path(), machineLearningModel.getAccuracy()  );
//            learningHistoryModelRepository.save(learningHistoryModel);
            machineLearningModelRepository.delete(machineLearningModel);

            return ResponseEntity.ok("Dữ liệu đã được sao chép sang lịch sử.");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy MachineLearningModel với ID: " + machineLearningId);

    }
    @GetMapping("/search")
    public String searchData(Model model,
                             @RequestParam("tu") String tu,
                             @RequestParam("den") String den,
                             @RequestParam("id") Integer id) {

        Date tuNgay = null;
        Date denNgay = null;

        if (!tu.isEmpty() && !den.isEmpty()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                tuNgay = new Date(dateFormat.parse(tu).getTime());
                denNgay = new Date(dateFormat.parse(den).getTime());
            } catch (java.text.ParseException e) {
                // Xử lý lỗi ở đây nếu ngày không hợp lệ
                e.printStackTrace(); // In lỗi ra để xem
            }
        }
        List<MachineLearningModel> models;

        if (id != 0) {
            models = machineLearningModelRepository.findByIdModel(id);
        } else if (tuNgay != null && denNgay != null) {
            models = machineLearningModelRepository.findByTrainingBetween(tuNgay, denNgay);
        }else {
            return "huan_luyen_model";
        }
        model.addAttribute("models", models);
        model.addAttribute("tu", tu);
        model.addAttribute("den", den);
        model.addAttribute("id", id);

        return "huan_luyen_model";
    }
}
