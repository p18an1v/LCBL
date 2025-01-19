package com.LeetcodeBeginners.service;

import com.LeetcodeBeginners.dto.UserResponseDTO;
import com.LeetcodeBeginners.entity.User;
import com.LeetcodeBeginners.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public UserResponseDTO getUserDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Map User entity to UserResponseDTO
        return modelMapper.map(user, UserResponseDTO.class);
    }
}
