package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.*;
import com.example.onlineshop.Repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final OrganizationConsiderationRepository organizationConsiderationRepository;

    public ResponseEntity<?> organizationAccess(String name, String operation) {
        Organization organization = organizationRepository.findByName(name);
        if (organization == null)
            return new ResponseEntity<>(Map.of("message", "Организация не найдена"), HttpStatus.NOT_FOUND);
        if (operation.equals("LOCK")) {
            organization.setFrozen(true);
            organizationRepository.save(organization);
            return new ResponseEntity<>(Map.of("message", "Организация заморожена"), HttpStatus.OK);
        }
        if (operation.equals("UNLOCK")) {
            organization.setFrozen(false);
            organizationRepository.save(organization);
            return new ResponseEntity<>(Map.of("message", "Организация разморожена"), HttpStatus.OK);
        }
        return new ResponseEntity<>(Map.of("message", "Операция не найдена"), HttpStatus.BAD_REQUEST);
    }

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
        organizationRepository.deleteById(id);
        return new ResponseEntity<>(Map.of("message", "Организация удалена."), HttpStatus.NO_CONTENT);
    }

    @Transactional
    public ResponseEntity<?> validateProduct(Long id, String action) {
        Product product = productRepository.findProductById(id);
        if (product == null)
            return new ResponseEntity<>(Map.of("message", "Такого товара нет"), HttpStatus.NOT_FOUND);
        if (action.equals("APPROVED")) {
            return approvedProduct(product);
        } else {
            return forbiddenProduct(product);
        }
    }

    private ResponseEntity<?> approvedProduct(Product product) {
        product.setFrozen(false);
        product.setGrade(0.0);
        Notification notification = new Notification();
        notification.setUser(product.getOrganization().getUser());
        notification.setTime(LocalDateTime.now());
        notification.setHeader(product.getOrganization().getUser().getUsername());
        notification.setAlert("Товар допущен к продаже");
        notificationRepository.save(notification);
        productRepository.save(product);
        return new ResponseEntity<>(Map.of("message", "Товар одобрен"), HttpStatus.OK);
    }

    private ResponseEntity<?> forbiddenProduct(Product product) {
        Notification notification = new Notification();
        notification.setUser(product.getOrganization().getUser());
        notification.setTime(LocalDateTime.now());
        notification.setHeader(product.getOrganization().getUser().getUsername());
        notification.setAlert("Товар допущен к продаже");
        notificationRepository.save(notification);
        return new ResponseEntity<>(Map.of("message", "Товар не одобрен"), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> validateOrganization(String name, String action) {
        OrganizationConsideration organizationConsideration = organizationConsiderationRepository.findByName(name);
        if (organizationConsideration == null)
            return new ResponseEntity<>(Map.of("message", "Такой организации нет"), HttpStatus.NOT_FOUND);
        if (action.equals("APPROVED")) {
            return approvedRegistration(organizationConsideration);
        } else {
            return forbiddenRegistration(organizationConsideration);
        }
    }

    private ResponseEntity<?> forbiddenRegistration(OrganizationConsideration organizationConsideration) {
        Notification notification = new Notification();
        notification.setUser(organizationConsideration.getUser());
        notification.setTime(LocalDateTime.now());
        notification.setHeader(organizationConsideration.getUser().getUsername());
        notification.setAlert("Организация не одобрена");
        notificationRepository.save(notification);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<?> approvedRegistration(OrganizationConsideration organizationConsideration) {
        Organization organization = new Organization();
        organization.setFrozen(false);
        organization.setUser(organizationConsideration.getUser());
        organization.setName(organizationConsideration.getName());
        organization.setBalance(organizationConsideration.getBalance());
        organization.setDescription(organizationConsideration.getDescription());
        Notification notification = new Notification();
        notification.setUser(organizationConsideration.getUser());
        notification.setTime(LocalDateTime.now());
        notification.setHeader(organizationConsideration.getUser().getUsername());
        notification.setAlert("Организация зарегистрирована");
        organizationConsiderationRepository.delete(organizationConsideration);
        notificationRepository.save(notification);
        organizationRepository.save(organization);
        return new ResponseEntity<>(Map.of("message", "Организация успешно зарегистрирована!"), HttpStatus.OK);
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
