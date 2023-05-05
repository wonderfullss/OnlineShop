package com.example.onlineshop.Controller;

import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;

    @PostMapping(value = "/shop/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        return authService.signUpUser(user);
    }

    @PostMapping(value = "/shop/sign-in")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        return authService.singInUser(email, password);
    }
}
