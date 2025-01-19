package com.LeetcodeBeginners.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

   // @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @GetMapping("/users")
//    public ResponseEntity<List<User>> getAllUsers() {
//        return ResponseEntity.ok(userRepository.findAll());
//    }
}
