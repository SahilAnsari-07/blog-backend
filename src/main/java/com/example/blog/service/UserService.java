package com.example.blog.service;

import com.example.blog.dto.UserRequest;
import com.example.blog.dto.UserResponse;
import com.example.blog.exception.DuplicateResourceException;
import com.example.blog.mapper.UserMapper;
import com.example.blog.model.User;
import com.example.blog.model.UserRole;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponse saveUser(UserRequest userRequest) {
        Map<String, String> error = new HashMap<>();
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            error.put("Username", "Username is already taken");
        }

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            error.put("Email", "Email is already used");
        }

        if (!error.isEmpty()) {
            throw new DuplicateResourceException(error);
        }

        User user = userMapper.toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        user.setRole(UserRole.ROLE_USER);

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);

    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }
}
