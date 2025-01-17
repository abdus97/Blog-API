package com.emumba.blogapi.service;


import com.emumba.blogapi.dto.UserProfileUpdateDto;
import com.emumba.blogapi.dto.UserRegistrationDto;
import com.emumba.blogapi.model.user.NonGitHubUserValidation;
import com.emumba.blogapi.model.user.Role;
import com.emumba.blogapi.model.user.User;
import com.emumba.blogapi.repository.RoleRepository;
import com.emumba.blogapi.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final Validator validator;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }

    public User registerUser(UserRegistrationDto registrationDTO) {
        // Check if the email or username is already in use
        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use.");
        }

        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use.");
        }

        // Create a new user and set properties
        User user = new User();
        user.setUsername(registrationDTO.getUsername());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword())); // Encode password

        // Assign roles to the user
        Set<Role> roles = new HashSet<>();
        for (String roleName : registrationDTO.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        user.setRoles(roles);
        Set<ConstraintViolation<User>> violations = validator.validate(user, NonGitHubUserValidation.class);
        if (!violations.isEmpty()) {
            throw new RuntimeException("Validation errors: " + violations);
        }
        // Save and return the user
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    public void updateUser(String email, UserProfileUpdateDto userProfileUpdateDTO) {
        User user = getUserByEmail(email);
        user.setUsername(userProfileUpdateDTO.getUsername());
        user.setEmail(userProfileUpdateDTO.getEmail());
        // Update other fields if needed
        userRepository.save(user);
    }

    public void updatePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Verify the current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // Update the password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }




}
