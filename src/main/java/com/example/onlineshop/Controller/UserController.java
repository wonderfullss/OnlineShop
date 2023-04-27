package com.example.onlineshop.Controller;

import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/user/myNotifications")
    public ResponseEntity<?> getAllNotifications() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.allNotificationsForUser(user.getId());
    }
}
