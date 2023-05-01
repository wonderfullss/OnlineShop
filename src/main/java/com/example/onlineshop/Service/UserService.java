package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.HistoryOrders;
import com.example.onlineshop.Entity.Organization;
import com.example.onlineshop.Entity.Product;
import com.example.onlineshop.Entity.User;
import com.example.onlineshop.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final NotificationRepository notificationRepository;

    private final OrganizationRepository organizationRepository;

    private final HistoryOrdersRepository historyOrdersRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ResponseEntity<?> getHistory() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(historyOrdersRepository.findAllByUserId(user.getId()), HttpStatus.OK);
    }

    public ResponseEntity<?> returnProduct(Long id) {
        HistoryOrders historyOrders = historyOrdersRepository.findHistoryOrdersById(id);
        if (historyOrders == null)
            return new ResponseEntity<>(Map.of("message", "Товара нет"), HttpStatus.CONFLICT);
        if (Duration.between(LocalDateTime.now(), historyOrders.getDate()).toHours() > 1) {
            return new ResponseEntity<>(Map.of("message", "Возврат запрещен"), HttpStatus.OK);
        }
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBalance(user.getBalance() + historyOrders.getPurchaseAmount());
        Product product = historyOrders.getProduct();
        product.setQuantityInStock(product.getQuantityInStock() + 1);
        Organization organization = organizationRepository.findOrganizationById(product.getOrganization().getId());
        organization.setBalance(organization.getBalance() - product.getPrice());
        userRepository.save(user);
        productRepository.save(product);
        return new ResponseEntity<>(Map.of("message", "Возврат оформлен"), HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> buyProduct(Long id) {
        Product product = productRepository.findProductById(id);
        if (product == null)
            return new ResponseEntity<>(Map.of("message", "Такого товара нет!"), HttpStatus.NOT_FOUND);
        if (product.getQuantityInStock().equals(0))
            return new ResponseEntity<>(Map.of("message", "Товара нет в наличии!"), HttpStatus.NO_CONTENT);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getBalance() < product.getPrice())
            return new ResponseEntity<>(Map.of("message", "Недостаточно средств."), HttpStatus.CONFLICT);
        product.setQuantityInStock(product.getQuantityInStock() - 1);
        user.setBalance(user.getBalance() - product.getPrice());
        Organization organization = organizationRepository.findOrganizationById(product.getOrganization().getId());
        organization.setBalance(organization.getBalance() + (product.getPrice() * 0.13));
        organizationRepository.save(organization);
        userRepository.save(user);
        productRepository.save(product);
        HistoryOrders historyOrders = new HistoryOrders();
        historyOrders.setUser(user);
        historyOrders.setPurchaseAmount(product.getPrice());
        historyOrders.setProduct(product);
        historyOrdersRepository.save(historyOrders);
        return new ResponseEntity<>(Map.of("message", "Поздравляем с покупкой!"), HttpStatus.OK);
    }

    public ResponseEntity<?> allNotificationsForUser(Long id) {
        return new ResponseEntity<>(notificationRepository.findAllByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<?> registerOrganization(Organization organization) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        organization.setUser(user);
        organizationRepository.save(organization);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
