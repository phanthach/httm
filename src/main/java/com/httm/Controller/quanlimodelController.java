package com.httm.Controller;

import com.httm.DAO.LearningHistoryModelRepository;
import com.httm.Model.LearningHistoryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/quan_li_model")
public class quanlimodelController {
    @Autowired
    public LearningHistoryModelRepository learningHistoryModelRepository;
    @GetMapping("")
    public String listModel(Model model){
        List<LearningHistoryModel> models = learningHistoryModelRepository.findAll();
        model.addAttribute("models", models);
        return "quan_li_model";
    }
    @GetMapping("/{id}")
    public ResponseEntity<LearningHistoryModel> getLearningModel(@PathVariable String id) {
        Optional<LearningHistoryModel> model = learningHistoryModelRepository.findById(id);
        if (model.isPresent()) {
            return new ResponseEntity<>(model.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LearningHistoryModel> updateLearningModel(
            @PathVariable String id,
            @RequestBody LearningHistoryModel updatedModel) {
        Optional<LearningHistoryModel> model = learningHistoryModelRepository.findById(id);
        if (model.isPresent()) {
            LearningHistoryModel existingModel = model.get();
            existingModel.setName_Model(updatedModel.getName_Model());
            existingModel.setTraining_Time(updatedModel.getTraining_Time());
            existingModel.setTraining_Time(updatedModel.getTraining_Time());
            learningHistoryModelRepository.save(existingModel);
            return new ResponseEntity<>(existingModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLearningModel(@PathVariable String id) {
        Optional<LearningHistoryModel> model = learningHistoryModelRepository.findById(id);
        if (model.isPresent()) {
            learningHistoryModelRepository.delete(model.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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

        List<LearningHistoryModel> models;

        if (id != 0) {
            models = learningHistoryModelRepository.findByIdModel(id);
        } else if (tuNgay != null && denNgay != null) {
            // Nếu không có ID, tìm theo ngày nếu cả hai ngày được nhập
            models = learningHistoryModelRepository.findByTrainingBetween(tuNgay, denNgay);
        }
        else {
            return "quan_li_model";
        }

        // Đưa dữ liệu vào model để hiển thị trên giao diện
        model.addAttribute("models", models);
        model.addAttribute("tu", tu);
        model.addAttribute("den", den);
        model.addAttribute("id", id);

        return "quan_li_model"; // Thay "your_template" bằng tên template của bạn
    }
}

