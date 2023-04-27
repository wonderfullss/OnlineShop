package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public ResponseEntity<?> upBalance(String username, Double balance) {
        User user = userRepository.findUserByUsername(username);
        user.setBalance(balance);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
