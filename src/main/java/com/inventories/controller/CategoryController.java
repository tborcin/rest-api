package com.inventories.controller;

import com.inventories.kafka.KafkaProducer;
import com.inventories.model.CategoryEntity;
import com.inventories.model.CustomMessage;
import com.inventories.service.CategoryService;
import com.inventories.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/product/categories")
public class CategoryController {
    public static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    CategoryService categoryService;

    @Autowired
    KafkaProducer kafkaProducer;

    @Value("${spring.kafka.consumer.group-id}")
    String kafkaGroupId;

    @Value("${inventories.kafka.post.category}")
    String postCategoryTopic;

    @GetMapping(value = "")
    public ResponseEntity<?> getAllCategory(){
        Iterable<CategoryEntity> category = null;
        try{
            category = categoryService.getAllCategory();
        } catch (Exception e) {
            logger.error("An error occurred! {}", e.getMessage());
            CustomErrorType.returnResponsEntityError(e.getMessage());
        }
        return new ResponseEntity<Iterable>(category, HttpStatus.OK);
    }

    @PostMapping(value = "", consumes = {"application/json", "application/soap+xml"})
    public ResponseEntity<?> addCategory(@RequestBody CategoryEntity categoryEntity){
        logger.info(("Process add new category"));
        CustomMessage customMessage = new CustomMessage();
        try {
            kafkaProducer.postCategory(postCategoryTopic, kafkaGroupId, categoryEntity);
            customMessage.setStatusCode(HttpStatus.OK.value());
            customMessage.setMessage("Created new category");
        } catch (Exception e) {
            logger.error("An error occurred! {}", e.getMessage());
            CustomErrorType.returnResponsEntityError(e.getMessage());
        }
        return new ResponseEntity<CustomMessage>(customMessage, HttpStatus.OK);
    }
}
