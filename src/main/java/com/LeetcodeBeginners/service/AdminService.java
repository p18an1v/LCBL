package com.LeetcodeBeginners.service;

import com.LeetcodeBeginners.entity.Question;
import com.LeetcodeBeginners.entity.Topic;
import com.LeetcodeBeginners.exception.ResourceNotFoundException;
import com.LeetcodeBeginners.repository.TopicRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private TopicRepository topicRepository;

    /**
     * Get all topics
     */
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    /**
     * Create a new topic
     */
    public Topic createTopic(Topic topic) {
        validateTopic(topic);
        return topicRepository.save(topic);
    }

    /**
     * Update an existing topic
     */
    public Topic updateTopic(String id, Topic updatedList) {
        validateTopic(updatedList);
        return topicRepository.findById(new ObjectId(id))
                .map(existing -> {
                    existing.setDataStructure(updatedList.getDataStructure());
                    existing.setQuestionsList(updatedList.getQuestionsList());
                    return topicRepository.save(existing);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + id));
    }

    /**
     * Delete a topic
     */
    public void deleteTopic(String id) {
        if (!topicRepository.existsById(new ObjectId(id))) {
            throw new ResourceNotFoundException("Topic not found with ID: " + id);
        }
        topicRepository.deleteById(new ObjectId(id));
    }

    /**
     * Get all questions for a specific topic
     */
    public List<Question> getQuestionsByTopic(String topicId) {
        Topic topic = topicRepository.findById(new ObjectId(topicId))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));
        return topic.getQuestionsList();
    }

    /**
     * Add a question to a topic
     */
    public Question addQuestionToTopic(String topicId, Question question) {
        validateQuestion(question);
        question.setQuestionId(new ObjectId()); // Generate a new ID for the question
        Topic topic = topicRepository.findById(new ObjectId(topicId))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));

        topic.getQuestionsList().add(question);
        topicRepository.save(topic);
        return question;
    }

    /**
     * Update a question in a topic
     */
    public Question updateQuestion(String topicId, String questionId, Question updatedQuestion) {
        validateQuestion(updatedQuestion);
        Topic topic = topicRepository.findById(new ObjectId(topicId))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));

        topic.getQuestionsList().stream()
                .filter(q -> q.getQuestionId().toString().equals(questionId))
                .findFirst()
                .ifPresentOrElse(
                        existing -> {
                            existing.setQuestionName(updatedQuestion.getQuestionName());
                            existing.setUrl(updatedQuestion.getUrl());
                            existing.setLevel(updatedQuestion.getLevel());
                            existing.setDataStructure(updatedQuestion.getDataStructure());
                        },
                        () -> { throw new ResourceNotFoundException("Question not found with ID: " + questionId); }
                );

        topicRepository.save(topic);
        return updatedQuestion;
    }

    /**
     * Delete a question from a topic
     */
    public void deleteQuestion(String topicId, String questionId) {
        Topic topic = topicRepository.findById(new ObjectId(topicId))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));

        boolean removed = topic.getQuestionsList().removeIf(q -> q.getQuestionId().toString().equals(questionId));
        if (!removed) {
            throw new ResourceNotFoundException("Question not found with ID: " + questionId);
        }

        topicRepository.save(topic);
    }

    /**
     * Validate topic before saving or updating
     */
    private void validateTopic(Topic topic) {
        if (topic.getDataStructure() == null || topic.getDataStructure().isEmpty()) {
            throw new IllegalArgumentException("DataStructure cannot be null or empty");
        }
    }

    /**
     * Validate question before saving or updating
     */
    private void validateQuestion(Question question) {
        if (question.getQuestionName() == null || question.getQuestionName().isEmpty()) {
            throw new IllegalArgumentException("Question name cannot be null or empty");
        }
        if (question.getUrl() == null || question.getUrl().isEmpty()) {
            throw new IllegalArgumentException("Question URL cannot be null or empty");
        }
        if (question.getLevel() == null) {
            throw new IllegalArgumentException("Question level cannot be null");
        }
    }
}