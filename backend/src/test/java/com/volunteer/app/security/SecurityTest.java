package com.volunteer.app.security;

import com.volunteer.app.entity.Role;
import com.volunteer.app.entity.User;
import com.volunteer.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SecurityTest {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    private String validToken;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setLogin("securitytest");
        user.setPassword("password123");
        user.setEmail("security@test.com");
        user.setFirstName("Test");
        user.setLastName("Security");
        user.setRole(Role.VOLUNTEER);
        userRepository.save(user);

        validToken = jwtService.generateToken("securitytest");
    }

    @Test
    void testJwtService_GenerateToken() {
        String token = jwtService.generateToken("securitytest");
        assertNotNull(token);
        assertTrue(token.length() > 50);
        assertEquals("securitytest", jwtService.extractUsername(token));
    }

    @Test
    void testJwtService_ValidateToken() {
        User user = userRepository.findByLogin("securitytest").get();
        assertTrue(jwtService.isTokenValid(validToken, new UserDetailsImpl(user)));
    }

    @Test
    void testJwtService_ExtractUsername() {
        assertEquals("securitytest", jwtService.extractUsername(validToken));
    }
}