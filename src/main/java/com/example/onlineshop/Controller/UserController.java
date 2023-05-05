package com.example.onlineshop.Controller;

import com.example.onlineshop.Entity.OrganizationConsideration;
import com.example.onlineshop.Entity.Review;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/product/all")
    public ResponseEntity<?> getAllProduct() {
        return userService.getProduct();
    }

    @PutMapping(value = "/product/review")
    public ResponseEntity<?> addReview(@RequestBody Review review, @RequestParam Long id) {
        return userService.addReview(review, id);
    }

    @PutMapping(value = "/user/buy")
    public ResponseEntity<?> buyProductByUser(@RequestParam Long id) {
        return userService.buyProduct(id);
    }

    @PutMapping(value = "/user/return/product")
    public ResponseEntity<?> returnProduct(@RequestParam Long id) {
        return userService.returnProduct(id);
    }

    @GetMapping(value = "/my/history")
    public ResponseEntity<?> getHistory() {
        return userService.getHistory();
    }

    @GetMapping(value = "/user/myNotifications")
    public ResponseEntity<?> getAllNotifications() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.allNotificationsForUser(user.getId());
    }

    @PostMapping(value = "/register/organization")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationConsideration organization) {
        return userService.registerOrganization(organization);
    }
}
