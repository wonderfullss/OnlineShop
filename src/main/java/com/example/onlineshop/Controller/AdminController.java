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

    @PostMapping(value = "/admin/validate/organization/{name}")
    public ResponseEntity<?> validateOrganization(@PathVariable String name, @RequestParam String action) {
        return adminService.validateOrganization(name, action);
    }

    @PutMapping("/admin/validate/product/{id}")
    public ResponseEntity<?> validateProduct(@PathVariable Long id, @RequestParam String action) {
        return adminService.validateProduct(id, action);
    }

    @PutMapping(value = "/admin/access/organization{name}")
    public ResponseEntity<?> organizationAccess(@PathVariable String name, @RequestParam String operation) {
        return adminService.organizationAccess(name, operation);
    }

    @PutMapping(value = "/admin/access/user/{username}")
    public ResponseEntity<?> userAccess(@PathVariable String username, @RequestParam String operation) {
        return adminService.userAccess(username, operation);
    }

    @DeleteMapping(value = "/admin/delete/organization/{id}")
    public ResponseEntity<?> deleteOrganization(@PathVariable Long id) {
        return adminService.deleteOrganization(id);
    }

    @DeleteMapping(value = "/admin/delete/user/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        return adminService.deleteUser(username);
    }

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
