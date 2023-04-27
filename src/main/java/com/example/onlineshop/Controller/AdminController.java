package com.example.onlineshop.Controller;

import com.example.onlineshop.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PutMapping(value = "/admin/balance")
    public ResponseEntity<?> upBalance(@RequestParam String username, @RequestParam Double balance) {
        return adminService.upBalance(username, balance);
    }
}
