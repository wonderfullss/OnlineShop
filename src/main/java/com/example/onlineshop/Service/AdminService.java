package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.Notification;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Repository.NotificationRepository;
import com.example.onlineshop.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    public ResponseEntity<?> upBalance(String username, Double balance) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Такого юзера нет!"), HttpStatus.BAD_REQUEST);
        user.setBalance(user.getBalance() + balance);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> notificationForUser(Notification notification, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Такого юзера нет!"), HttpStatus.BAD_REQUEST);
        notification.setUser(user);
        notificationRepository.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
