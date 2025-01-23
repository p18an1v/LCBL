package com.LeetcodeBeginners.controller;


import com.LeetcodeBeginners.dto.QuestionDTO;
import com.LeetcodeBeginners.dto.TopicDTO;
import com.LeetcodeBeginners.entity.Question;
import com.LeetcodeBeginners.entity.Topic;
import com.LeetcodeBeginners.service.AdminService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final ModelMapper modelMapper;
    private final AdminService adminService;

    public AdminController(ModelMapper modelMapper, AdminService adminService) {
        this.modelMapper = modelMapper;
        this.adminService = adminService;
    }

    /**
     * Get all topics
     */
    @GetMapping("/topics")
    public ResponseEntity<List<TopicDTO>> getAllTopics() {
        List<TopicDTO> topics = adminService.getAllTopics();
        List<TopicDTO> dtos = topics.stream()
                .map(topic -> modelMapper.map(topic, TopicDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Create a new topic
     */
    @PostMapping("/topics")
    public ResponseEntity<TopicDTO> createTopic(@RequestBody @Valid TopicDTO topicDTO) {
        TopicDTO savedTopic = adminService.createTopic(topicDTO);
        return ResponseEntity.ok(savedTopic);
    }


    /**
     * Update an existing topic
     */
    @PutMapping("/topics/{id}")
    public ResponseEntity<TopicDTO> updateTopic(@PathVariable String id, @RequestBody @Valid TopicDTO topicDTO) {
        TopicDTO updatedTopic = adminService.updateTopic(id, topicDTO);
        return ResponseEntity.ok(updatedTopic);
    }

    /**
     * Delete a topic
     */
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String id) {
        adminService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get all questions in a specific topic
     */
    @GetMapping("/topics/{topicId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByTopic(@PathVariable String topicId) {
        List<QuestionDTO> questions = adminService.getQuestionsByTopic(topicId);
        List<QuestionDTO> dtos = questions.stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    /**
     * Add a question to a specific topic
     */
    @PostMapping("/topics/{topicId}/questions")
    public ResponseEntity<QuestionDTO> addQuestionToTopic(
            @PathVariable String topicId,
            @RequestBody @Valid QuestionDTO questionDTO) {
        QuestionDTO savedQuestion = adminService.addQuestionToTopic(topicId, questionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
    }

    /**
     * Update a question in a specific topic
     */
    @PutMapping("/topics/{topicId}/questions/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable String topicId,
            @PathVariable String questionId,
            @RequestBody @Valid QuestionDTO updatedQuestionDTO) {
        QuestionDTO updatedQuestion = adminService.updateQuestion(questionId, updatedQuestionDTO);
        return ResponseEntity.ok(updatedQuestion);
    }


    /**
     * Delete a question from a specific topic
     */
    @DeleteMapping("/topics/{topicId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable String topicId,
            @PathVariable String questionId) {
        adminService.deleteQuestion(topicId, questionId);
        return ResponseEntity.noContent().build();
    }
}
