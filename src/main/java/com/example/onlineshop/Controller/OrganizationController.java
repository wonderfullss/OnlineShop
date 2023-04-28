package com.example.onlineshop.Controller;

import com.example.onlineshop.Entity.Product;
import com.example.onlineshop.Service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping(value = "/organization/product/add")
    public ResponseEntity<?> addProduct(@RequestBody Product product, @RequestParam String orgName) {
        return organizationService.addProduct(product, orgName);
    }
}
