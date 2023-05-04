package com.example.onlineshop.Controller;

import com.example.onlineshop.Entity.Notification;
import com.example.onlineshop.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping(value = "/admin/orderList/{id}")
    public ResponseEntity<?> getUserOrderList(@PathVariable Long id) {
        return adminService.getOrderList(id);
    }

    @GetMapping(value = "/admin/user/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return adminService.getUser(id);
    }

    @PutMapping(value = "/admin/balance")
    public ResponseEntity<?> upBalance(@RequestParam String username, @RequestParam Double balance) {
        return adminService.upBalance(username, balance);
    }

    @PutMapping(value = "/admin/alert")
    public ResponseEntity<?> notification(@RequestBody Notification notification, @RequestParam String username) {
        return adminService.notificationForUser(notification, username);
    }
}
