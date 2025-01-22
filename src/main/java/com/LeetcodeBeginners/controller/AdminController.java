package com.LeetcodeBeginners.controller;


import com.LeetcodeBeginners.dto.TopicDTO;
import com.LeetcodeBeginners.entity.Question;
import com.LeetcodeBeginners.entity.Topic;
import com.LeetcodeBeginners.service.AdminService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

   // @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        return ResponseEntity.ok(userRepository.findAll());
//    }

    //endpoints to

    private ModelMapper modelMapper;
    private AdminService adminService;

    AdminController(ModelMapper modelMapper, AdminService adminService) {
        this.modelMapper = modelMapper;
        this.adminService = adminService;
    }

    @GetMapping
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<Topic> topics = adminService.getAllTopics();
        List<TopicDTO> dtos = topics.stream()
                .map(topic -> modelMapper.map(topic, TopicDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<TopicDTO> createTopic(@RequestBody TopicDTO topicDTO) {
        Topic savedTopic = adminService.createTopic(
                modelMapper.map(topicDTO, Topic.class));
        return ResponseEntity.ok(modelMapper.map(savedTopic, TopicDTO.class));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TopicDTO> updateTopic(@PathVariable String id, @RequestBody TopicDTO topicDTO) {
        Topic updatedTopic = adminService.updateTopic(
                id, modelMapper.map(topicDTO, Topic.class));
        return ResponseEntity.ok(modelMapper.map(updatedTopic, TopicDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String id) {
        adminService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    //questions endpoints
    // Get all questions in a specific topic
    @GetMapping("/topics/{topicId}/questions")
    public ResponseEntity<List<Question>> getQuestionsByTopic(@PathVariable String topicId) {
        return ResponseEntity.ok(adminService.getQuestionsByTopic(topicId));
    }

    // Add a question to a specific topic
    @PostMapping("/topics/{topicId}/questions")
    public ResponseEntity<Question> addQuestionToTopic(
            @PathVariable String topicId,
            @RequestBody Question question) {
        return ResponseEntity.ok(adminService.addQuestionToTopic(topicId, question));
    }

    // Update a question in a specific topic
    @PutMapping("/topics/{topicId}/questions/{questionId}")
    public ResponseEntity<Question> updateQuestion(
            @PathVariable String topicId,
            @PathVariable String questionId,
            @RequestBody Question updatedQuestion) {
        return ResponseEntity.ok(adminService.updateQuestion(topicId, questionId, updatedQuestion));
    }

    // Delete a question from a topic
    @DeleteMapping("/topics/{topicId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable String topicId,
            @PathVariable String questionId) {
        adminService.deleteQuestion(topicId, questionId);
        return ResponseEntity.noContent().build();
    }
}
