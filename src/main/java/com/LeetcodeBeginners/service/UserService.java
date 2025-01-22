package com.LeetcodeBeginners.service;

import com.LeetcodeBeginners.dto.UserProgressDTO;
import com.LeetcodeBeginners.entity.UserProgress;
import com.LeetcodeBeginners.exception.ResourceNotFoundException;
import com.LeetcodeBeginners.repository.UserProgressRepository;
import org.bson.types.ObjectId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    @Autowired
    private ModelMapper modelMapper;

    // Fetch user progress
    public UserProgressDTO getUserProgress(String userId) {
        // Log userId
        System.out.println("Fetching progress for userId: " + userId);

        UserProgress userProgress = userProgressRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> {
                    System.out.println("User not found with ID: " + userId);
                    return new ResourceNotFoundException("User not found with ID: " + userId);
                });

        // Log fetched data
        System.out.println("User progress fetched: " + userProgress);
        return mapToDTO(userProgress);
    }

    // Track a solved question
    public synchronized UserProgressDTO trackQuestion(String userId, String questionId) {
        UserProgress userProgress = userProgressRepository.findById(new ObjectId(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // If the question is not already marked as completed
        if (!userProgress.getCompletedQuestions().contains(questionId)) {
            userProgress.getCompletedQuestions().add(questionId);
            userProgress.setTotalSolved(userProgress.getCompletedQuestions().size()); // Update solved count
            userProgressRepository.save(userProgress); // Save progress
        }

        return mapToDTO(userProgress);
    }

    // Map Entity to DTO
    private UserProgressDTO mapToDTO(UserProgress userProgress) {
        return modelMapper.map(userProgress, UserProgressDTO.class);
    }
}
