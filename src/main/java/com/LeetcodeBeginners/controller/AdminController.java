package com.LeetcodeBeginners.controller;


import com.LeetcodeBeginners.dto.PatternDTO;
import com.LeetcodeBeginners.dto.QuestionDTO;
import com.LeetcodeBeginners.dto.TopicDTO;
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

    /*-----------------------------------Topic-Routes-Start--------------------------------------------*/
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

    @GetMapping("/topics/{topicId}")
    public ResponseEntity<TopicDTO> getTopicById(@PathVariable String topicId) {
        TopicDTO topic = adminService.getTopicById(topicId);
        return ResponseEntity.ok(topic);
    }


    /**
     * Delete a topic
     */
    @DeleteMapping("/topics/{id}")
    public ResponseEntity<Void> deleteTopic(@PathVariable String id) {
        adminService.deleteTopic(id);
        return ResponseEntity.noContent().build();
    }
    /*-----------------------------------Topic-Routes-End--------------------------------------------*/

    /*-----------------------------------Pattern-Routes-Start--------------------------------------------*/

    /*
        * Create a new pattern
     */
    @PostMapping("/patterns")
    public ResponseEntity<PatternDTO> createPattern(@RequestBody PatternDTO patternDTO) {
        return ResponseEntity.ok(adminService.createPattern(patternDTO));
    }

    /**
     * Get all patterns
     */
    @GetMapping("/patterns")
    public ResponseEntity<List<PatternDTO>> getAllPatterns() {
        List<PatternDTO> patterns = adminService.getAllPatterns();
        return ResponseEntity.ok(patterns);
    }

    /**
     * Update an existing pattern
     */
    @PutMapping("/patterns/{patternId}")
    public ResponseEntity<PatternDTO> updatePattern(
            @PathVariable String patternId,
            @RequestBody @Valid PatternDTO patternDTO) {
        PatternDTO updatedPattern = adminService.updatePattern(patternId, patternDTO);
        return ResponseEntity.ok(updatedPattern);
    }

    /**
     * Delete a pattern
     */
    @DeleteMapping("/patterns/{patternId}")
    public ResponseEntity<Void> deletePattern(@PathVariable String patternId) {
        adminService.deletePattern(patternId);
        return ResponseEntity.noContent().build();
    }
    /*-----------------------------------Pattern-Routes-End--------------------------------------------*/

    /*-----------------------------------Pattern-Questions-Routes-Start--------------------------------------------*/

    /**
     * Add a question to a pattern
     */
    @PostMapping("/patterns/{patternId}/questions")
    public ResponseEntity<QuestionDTO> addQuestionToPattern(
            @PathVariable String patternId,
            @RequestBody QuestionDTO questionDTO) {
        return ResponseEntity.ok(adminService.addQuestionToPattern(patternId, questionDTO));
    }

    /**
     * Get all questions in a pattern
     */
    @GetMapping("/patterns/{patternId}/questions")
    public ResponseEntity<List<QuestionDTO>> getQuestionsByPattern(@PathVariable String patternId) {
        return ResponseEntity.ok(adminService.getQuestionsByPattern(patternId));
    }


    /**
     * Update a question in a pattern
     */
    @PutMapping("/patterns/{patternId}/questions/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestionInPattern(
            @PathVariable String patternId,
            @PathVariable String questionId,
            @RequestBody QuestionDTO updatedQuestionDTO) {
        return ResponseEntity.ok(adminService.updateQuestionInPattern(patternId, questionId, updatedQuestionDTO));
    }

    /**
     * Delete a question from a pattern
     */
    @DeleteMapping("/patterns/{patternId}/questions/{questionId}")
    public ResponseEntity<Void> deleteQuestionFromPattern(
            @PathVariable String patternId,
            @PathVariable String questionId) {
        adminService.deleteQuestionFromPattern(patternId, questionId);
        return ResponseEntity.noContent().build();
    }
    /*-----------------------------------Pattern-Questions-Routes-End--------------------------------------------*/

    /*-----------------------------------Topic-Quetions-Routes-Start--------------------------------------------*/
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
    /*-----------------------------------Question-Routes-End--------------------------------------------*/
}
