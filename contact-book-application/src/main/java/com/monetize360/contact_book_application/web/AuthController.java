package com.monetize360.contact_book_application.web;

import com.monetize360.contact_book_application.dao.UserRepository;
import com.monetize360.contact_book_application.domain.User;
import com.monetize360.contact_book_application.dto.LoginDTO;
import com.monetize360.contact_book_application.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
    public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/generate-token")
    public String generateToken(@RequestBody LoginDTO authRequest) {
        String username = authRequest.getUsername();
        String password = authRequest.getPassword();

        // Validate the credentials
        if (validateCredentials(username, password)) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    private boolean validateCredentials(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return password.equals(user.getPassword());
        }
        return false;
    }
}
