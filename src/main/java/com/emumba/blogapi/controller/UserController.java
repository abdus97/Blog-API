package com.emumba.blogapi.controller;


import com.emumba.blogapi.dto.PasswordUpdateRequestDto;
import com.emumba.blogapi.dto.UserProfileUpdateDto;
import com.emumba.blogapi.dto.UserRegistrationDto;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto userRegistrationDTO) {
        userService.registerUser(userRegistrationDTO);
        return new ResponseEntity<>("User registered successfully!", HttpStatus.CREATED);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        String email = principal.getName();
        User user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateProfile(@Valid @RequestBody UserProfileUpdateDto profileUpdateDTO, Principal principal) {
        String email = principal.getName();
        userService.updateUser(email, profileUpdateDTO);
        return ResponseEntity.ok("Profile updated successfully!");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequestDto request, Principal principal) {
        String email = principal.getName();
        userService.updatePassword(email, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully!");
    }



}
