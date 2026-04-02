package com.lms.service;

import com.lms.dto.AuthRequest;
import com.lms.dto.AuthResponse;
import com.lms.dto.RegisterRequest;
import com.lms.entity.User;
import com.lms.repository.UserRepository;
import com.lms.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @InjectMocks private AuthService authService;

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setName("John"); request.setEmail("john@test.com");
        request.setPassword("pass"); request.setRole(User.Role.STUDENT);

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> { User u = i.getArgument(0); u.setId(1L); return u; });
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        AuthResponse response = authService.register(request);
        assertNotNull(response);
        assertEquals("token", response.getToken());
        assertEquals("STUDENT", response.getRole());
    }

    @Test
    void testRegister_EmailExists_ThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("exists@test.com"); request.setPassword("pass"); request.setName("Test");
        when(userRepository.existsByEmail("exists@test.com")).thenReturn(true);
        assertThrows(RuntimeException.class, () -> authService.register(request));
    }

    @Test
    void testLogin_Success() {
        AuthRequest request = new AuthRequest();
        request.setEmail("user@test.com"); request.setPassword("pass");

        User user = User.builder().id(1L).email("user@test.com").password("encoded")
            .name("User").role(User.Role.STUDENT).build();

        when(userRepository.findByEmail("user@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("token");

        AuthResponse response = authService.login(request);
        assertEquals("token", response.getToken());
    }

    @Test
    void testLogin_InvalidCredentials_ThrowsException() {
        AuthRequest request = new AuthRequest();
        request.setEmail("bad@test.com"); request.setPassword("wrong");
        when(userRepository.findByEmail("bad@test.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}
