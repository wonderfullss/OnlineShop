package com.example.onlineshop.Service;

import com.example.onlineshop.Repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final NotificationRepository notificationRepository;

    public ResponseEntity<?> allNotificationsForUser(Long id) {
        return new ResponseEntity<>(notificationRepository.findAllByUserId(id), HttpStatus.OK);
    }
}
