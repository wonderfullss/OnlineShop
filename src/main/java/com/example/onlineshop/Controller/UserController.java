package com.example.onlineshop.Controller;

import com.example.onlineshop.Entity.Organization;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping(value = "/register/organization")
    public ResponseEntity<?> registerOrganization(@RequestBody Organization organization) {
        return userService.registerOrganization(organization);
    }
}
