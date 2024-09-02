package com.monetize360.contact_book_application.web;

import com.monetize360.contact_book_application.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
    public class AuthController {

        @Autowired
        private JwtUtil jwtUtil;

        @GetMapping("/generate-token")
        public String generateToken(@RequestParam String username) {
            return jwtUtil.generateToken(username);
        }
}
