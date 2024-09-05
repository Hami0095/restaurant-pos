package com.abdmantech.user_service.service;

import com.abdmantech.user_service.dto.UserDTO;
import com.abdmantech.user_service.exception.UserAlreadyExistsException;
import com.abdmantech.user_service.model.User;
import com.abdmantech.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());

        if (existingUser.isPresent()) {
            // Return the existing user with a message
            throw new UserAlreadyExistsException("User with email " + userDTO.getEmail() + " already exists.");
        }
        // Create a new User entity
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());

        // Encode the password before setting it
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));


        user.setRole(User.Role.valueOf(userDTO.getRole()));
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());

        // Save the user entity to the database
        user = userRepository.save(user);

        // Convert the saved User entity to UserDTO and return it
        return toDTO(user);
    }

    @Override
    public UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPasswordHash(user.getPasswordHash()); // Consider removing this for security
        userDTO.setRole(user.getRole().name());
        // Convert Instant to OffsetDateTime with a specific time zone (UTC in this case)
        userDTO.setCreatedAt(OffsetDateTime.ofInstant(user.getCreatedAt(), ZoneOffset.UTC));
        userDTO.setUpdatedAt(OffsetDateTime.ofInstant(user.getUpdatedAt(), ZoneOffset.UTC));

        return userDTO;
    }
    @Override
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::toDTO);
    }

    @Override
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(this::toDTO);
    }

}
