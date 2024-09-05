package com.abdmantech.user_service.service;

import com.abdmantech.user_service.dto.UserDTO;
import com.abdmantech.user_service.model.User;

import java.util.Optional;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    Optional<UserDTO> getUserById(Long id);
    Optional<UserDTO> getUserByEmail(String email);
    UserDTO toDTO(User user);
}