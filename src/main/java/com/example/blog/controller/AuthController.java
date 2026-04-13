package com.example.blog.controller;


import com.example.blog.dto.JwtAuthResponse;
import com.example.blog.dto.LoginRequest;
import com.example.blog.dto.UserRequest;
import com.example.blog.dto.UserResponse;
import com.example.blog.model.User;
import com.example.blog.security.JwtUtil;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;



    @PostMapping("/register")
    public ResponseEntity<UserResponse>  register(@Valid @RequestBody UserRequest userRequest){
            UserResponse userResponse = userService.saveUser(userRequest);
            return ResponseEntity.ok(userResponse);


    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            User user = userService.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found after authentication"));
            String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
            JwtAuthResponse response = new JwtAuthResponse(token, user.getUserName(), user.getRole().name());
            return ResponseEntity.ok(response);

        }catch (BadCredentialsException e){
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid Email or Password"
            );

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(problemDetail);


        }


    }
}
