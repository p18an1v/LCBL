package com.LeetcodeBeginners.service;

import com.LeetcodeBeginners.dto.PatternDTO;
import com.LeetcodeBeginners.dto.QuestionDTO;
import com.LeetcodeBeginners.dto.TopicDTO;
import com.LeetcodeBeginners.entity.Pattern;
import com.LeetcodeBeginners.entity.Question;
import com.LeetcodeBeginners.entity.Topic;
import com.LeetcodeBeginners.exception.ResourceNotFoundException;
import com.LeetcodeBeginners.repository.PatternRepository;
import com.LeetcodeBeginners.repository.QuestionRepository;
import com.LeetcodeBeginners.repository.TopicRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private PatternRepository patternRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ModelMapper modelMapper;



    /*----------------------------------------------Topic-Start--------------------------------------------*/
    /**
     * Get all topics
     */
    public List<TopicDTO> getAllTopics() {
        List<Topic> topics = topicRepository.findAll();

        return topics.stream()
                .map(topic -> {
                    // Map Topic to TopicDTO
                    TopicDTO topicDTO = modelMapper.map(topic, TopicDTO.class);

                    // Convert ObjectId list to String list
                    List<String> questionIds = topic.getQuestionIds()
                            .stream()
                            .map(ObjectId::toString) // Convert ObjectId to String
                            .toList();

                    topicDTO.setQuestionIds(questionIds);
                    return topicDTO;
                })
                .toList();
    }

    /**
     * Create a new topic and initialize its question collection
     */
    public TopicDTO createTopic(TopicDTO topicDTO) {
        validateTopic(topicDTO);

        // Map DTO to entity
        Topic topic = modelMapper.map(topicDTO, Topic.class);

        // Initialize questionIds as an empty list
        topic.setQuestionIds(new ArrayList<>());

        // Save the topic
        Topic savedTopic = topicRepository.save(topic);

        // Convert the saved topic back to DTO
        TopicDTO savedTopicDTO = modelMapper.map(savedTopic, TopicDTO.class);

        // Ensure questionIds is set as empty in the DTO
        savedTopicDTO.setQuestionIds(new ArrayList<>());

        return savedTopicDTO;
    }

    /**
     * Update an existing topic
     */
    public TopicDTO updateTopic(String id, TopicDTO updatedTopicDTO) {
        validateTopic(updatedTopicDTO);

        Topic topic = topicRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + id));

        topic.setDataStructure(updatedTopicDTO.getDataStructure());
        Topic updatedTopic = topicRepository.save(topic);

        return modelMapper.map(updatedTopic, TopicDTO.class);
    }

    public TopicDTO getTopicById(String topicId) {
        Topic topic = topicRepository.findById(new ObjectId(topicId))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));

        // Convert the Topic to TopicDTO
        TopicDTO topicDTO = modelMapper.map(topic, TopicDTO.class);
        List<String> questionIds = topic.getQuestionIds()
                .stream()
                .map(ObjectId::toString) // Convert ObjectId to String
                .toList();

        topicDTO.setQuestionIds(questionIds);
        return topicDTO;
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
    /*----------------------------------------------Topic-End--------------------------------------------*/

    /*----------------------------------------------Pattern-Start--------------------------------------------*/
    public List<PatternDTO> getAllPatterns() {
        List<Pattern> patterns = patternRepository.findAll();
        return patterns.stream()
                .map(pattern -> {
                    PatternDTO patternDTO = modelMapper.map(pattern, PatternDTO.class);
                    patternDTO.setQuestionIds(
                            pattern.getQuestionIds().stream().toList()
                    );
                    return patternDTO;
                })
                .toList();
    }

    public PatternDTO createPattern(PatternDTO patternDTO) {
        Pattern pattern = modelMapper.map(patternDTO, Pattern.class);
        pattern.setQuestionIds(new ArrayList<>());
        Pattern savedPattern = patternRepository.save(pattern);
        return modelMapper.map(savedPattern, PatternDTO.class);
    }

    public QuestionDTO addQuestionToPattern(String patternId, QuestionDTO questionDTO) {
        // Convert patternId from String to ObjectId
        Pattern pattern = patternRepository.findById(new ObjectId(patternId))
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found"));

        // Map DTO to entity and generate a new ObjectId for the question
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setQuestionId(new ObjectId());
        question.setTopicId(new ObjectId(patternId)); // Optional if questions reference the pattern
        questionRepository.save(question);

        // Add the question ID to the pattern
        pattern.getQuestionIds().add(question.getQuestionId().toString()); // Store as String
        patternRepository.save(pattern);

        // Return the saved question as DTO
        return modelMapper.map(question, QuestionDTO.class);
    }


    public List<QuestionDTO> getQuestionsByPattern(String patternId) {
        // Convert patternId from String to ObjectId
        Pattern pattern = patternRepository.findById(new ObjectId(patternId))
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found"));

        // Fetch questions by their IDs
        return pattern.getQuestionIds().stream()
                .map(questionId -> {
                    Question question = questionRepository.findById(new ObjectId(questionId))
                            .orElseThrow(() -> new ResourceNotFoundException("Question not found"));
                    return modelMapper.map(question, QuestionDTO.class);
                })
                .toList();
    }

    /**
     * Update an existing pattern
     */
    public PatternDTO updatePattern(String patternId, PatternDTO updatedPatternDTO) {
        Pattern pattern = patternRepository.findById(new ObjectId(patternId))
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found with ID: " + patternId));

        // Update fields
        pattern.setPattern(updatedPatternDTO.getPattern());
        // Save the updated pattern
        Pattern updatedPattern = patternRepository.save(pattern);

        return modelMapper.map(updatedPattern, PatternDTO.class);
    }

    /**
     * Delete a pattern
     */
    public void deletePattern(String patternId) {
        if (!patternRepository.existsById(new ObjectId(patternId))) {
            throw new ResourceNotFoundException("Pattern not found with ID: " + patternId);
        }
        patternRepository.deleteById(new ObjectId(patternId));
    }


    /**
     * Update a question in a pattern
     */
    public QuestionDTO updateQuestionInPattern(String patternId, String questionId, QuestionDTO updatedQuestionDTO) {
        Pattern pattern = patternRepository.findById(new ObjectId(patternId))
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found"));

        if (!pattern.getQuestionIds().contains(questionId)) {
            throw new ResourceNotFoundException("Question not found in the pattern");
        }

        Question question = questionRepository.findById(new ObjectId(questionId))
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        // Update question fields
        question.setQuestionName(updatedQuestionDTO.getQuestionName());
        question.setUrl(updatedQuestionDTO.getUrl());
        question.setLevel(updatedQuestionDTO.getLevel());
        question.setDataStructure(updatedQuestionDTO.getDataStructure());
        questionRepository.save(question);

        return modelMapper.map(question, QuestionDTO.class);
    }

    /**
     * Delete a question from a pattern
     */
    public void deleteQuestionFromPattern(String patternId, String questionId) {
        Pattern pattern = patternRepository.findById(new ObjectId(patternId))
                .orElseThrow(() -> new ResourceNotFoundException("Pattern not found"));

        if (!pattern.getQuestionIds().remove(questionId)) {
            throw new ResourceNotFoundException("Question not found in the pattern");
        }

        patternRepository.save(pattern); // Update pattern without the question
        questionRepository.deleteById(new ObjectId(questionId)); // Remove question from DB
    }


    /*----------------------------------------------Pattern-End--------------------------------------------*/

    /*----------------------------------------------Question-Start--------------------------------------------*/
    /**
     * Get all questions for a specific topic
     */
    public List<QuestionDTO> getQuestionsByTopic(String topicId) {
        List<Question> questions = questionRepository.findByTopicId(new ObjectId(topicId));
        return questions.stream()
                .map(question -> modelMapper.map(question, QuestionDTO.class))
                .toList();
    }

    /**
     * Add a question to a topic
     */
    public QuestionDTO addQuestionToTopic(String topicId, QuestionDTO questionDTO) {
        validateQuestion(questionDTO);

        // Map DTO to entity
        Question question = modelMapper.map(questionDTO, Question.class);
        question.setQuestionId(new ObjectId());
        question.setTopicId(new ObjectId(topicId));

        // Save the question
        Question savedQuestion = questionRepository.save(question);

        // Update the topic with the new question ID
        Topic topic = topicRepository.findById(new ObjectId(topicId))
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with ID: " + topicId));
        topic.getQuestionIds().add(savedQuestion.getQuestionId());
        topicRepository.save(topic);

        return modelMapper.map(savedQuestion, QuestionDTO.class);
    }


    /**
     * Update a question in a topic
     */
    public QuestionDTO updateQuestion(String questionId, QuestionDTO updatedQuestionDTO) {
        validateQuestion(updatedQuestionDTO);

        Question question = questionRepository.findById(new ObjectId(questionId))
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        question.setQuestionName(updatedQuestionDTO.getQuestionName());
        question.setUrl(updatedQuestionDTO.getUrl());
        question.setLevel(updatedQuestionDTO.getLevel());
        question.setDataStructure(updatedQuestionDTO.getDataStructure());

        Question updatedQuestion = questionRepository.save(question);
        return modelMapper.map(updatedQuestion, QuestionDTO.class);
    }

    /**
     * Delete a question
     */
    public void deleteQuestion(String topicId, String questionId) {
        Question question = questionRepository.findById(new ObjectId(questionId))
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID: " + questionId));

        if (!question.getTopicId().toHexString().equals(topicId)) {
            throw new ResourceNotFoundException("Question does not belong to topic with ID: " + topicId);
        }

        questionRepository.deleteById(new ObjectId(questionId));
    }
    /*----------------------------------------------Question-End--------------------------------------------*/

    /**
     * Validate topic before saving or updating
     */
    private void validateTopic(TopicDTO topicDTO) {
        if (topicDTO.getDataStructure() == null || topicDTO.getDataStructure().isEmpty()) {
            throw new IllegalArgumentException("DataStructure cannot be null or empty");
        }
    }

    /**
     * Validate question before saving or updating
     */
    private void validateQuestion(QuestionDTO questionDTO) {
        if (questionDTO.getQuestionName() == null || questionDTO.getQuestionName().isEmpty()) {
            throw new IllegalArgumentException("Question name cannot be null or empty");
        }
        if (questionDTO.getUrl() == null || questionDTO.getUrl().isEmpty()) {
            throw new IllegalArgumentException("Question URL cannot be null or empty");
        }
        if (questionDTO.getLevel() == null || questionDTO.getLevel().isEmpty()) {
            throw new IllegalArgumentException("Question level cannot be null or empty");
        }
    }
}
