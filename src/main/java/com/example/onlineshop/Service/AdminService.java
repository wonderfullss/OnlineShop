package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.*;
import com.example.onlineshop.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    private final HistoryOrdersRepository historyOrdersRepository;

    private final OrganizationRepository organizationRepository;

    private final ProductRepository productRepository;

    public ResponseEntity<?> userAccess(String username, String operation) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Пользователь не найден"), HttpStatus.NOT_FOUND);
        if (operation.equals("LOCK")) {
            user.setAccountNonLocked(false);
            userRepository.save(user);
            return new ResponseEntity<>(Map.of("message", "Пользователь заблокирован"), HttpStatus.OK);
        }
        if (operation.equals("UNLOCK")) {
            user.setAccountNonLocked(true);
            userRepository.save(user);
            return new ResponseEntity<>(Map.of("message", "Пользователь разблокирован"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("message", "Операция не найдена"), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteOrganization(Long id) {
        List<Product> list = productRepository.findAllByOrganizationId(id);
        for (Product product : list) {
            product.setOrganization(null);
        }
        productRepository.saveAll(list);
        organizationRepository.deleteById(id);
        return new ResponseEntity<>(Map.of("message", "Организация удалена."), HttpStatus.NO_CONTENT);
    }

    @Transactional
    public ResponseEntity<?> deleteUser(String username) {
        if (userRepository.findUserByUsername(username) == null)
            return new ResponseEntity<>(Map.of("message", "Пользователь не найден"), HttpStatus.NOT_FOUND);
        userRepository.deleteUserByUsername(username);
        return new ResponseEntity<>(Map.of("message", "Пользователь удален."), HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<?> getUser(Long id) {
        User user = userRepository.findUserById(id);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Пользователь не найден."), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> getOrderList(Long id) {
        List<HistoryOrders> historyOrders = historyOrdersRepository.findAllByUserId(id);
        if (userRepository.findById(id).isEmpty())
            return new ResponseEntity<>(Map.of("message", "Пользователь не найден."), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(historyOrders, HttpStatus.OK);
    }

    public ResponseEntity<?> upBalance(String username, Double balance) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Такого юзера нет!"), HttpStatus.BAD_REQUEST);
        user.setBalance(user.getBalance() + balance);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> notificationForUser(Notification notification, String username) {
        User user = userRepository.findUserByUsername(username);
        if (user == null)
            return new ResponseEntity<>(Map.of("message", "Такого юзера нет!"), HttpStatus.BAD_REQUEST);
        notification.setUser(user);
        notificationRepository.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
