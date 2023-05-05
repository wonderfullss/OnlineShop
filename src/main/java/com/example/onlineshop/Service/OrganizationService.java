package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.Organization;
import com.example.onlineshop.Entity.Product;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Repository.OrganizationRepository;
import com.example.onlineshop.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    private final ProductRepository productRepository;

    public ResponseEntity<?> addProduct(Product product, String orgName) {
        Organization organization = organizationRepository.findByName(orgName);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (organization == null)
            return new ResponseEntity<>(Map.of("message", "Такой организации нет!"), HttpStatus.NOT_FOUND);
        if (!organization.getUser().getId().equals(user.getId())) {
            return new ResponseEntity<>(Map.of("message", "Нет доступа к организации"), HttpStatus.FORBIDDEN);
        }
        product.setFrozen(true);
        product.setOrganization(organization);
        productRepository.save(product);
        return new ResponseEntity<>(Map.of("message", "Товар успешно добавлен."), HttpStatus.OK);
    }
}
