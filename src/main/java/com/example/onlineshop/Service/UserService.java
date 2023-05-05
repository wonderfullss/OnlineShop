package com.example.onlineshop.Service;

import com.example.onlineshop.Entity.*;
import com.example.onlineshop.Repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final NotificationRepository notificationRepository;

    private final OrganizationRepository organizationRepository;

    private final HistoryOrdersRepository historyOrdersRepository;

    private final ProductRepository productRepository;

    private final OrganizationConsiderationRepository organizationConsiderationRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    private final PurchaseProductRepository purchaseProductRepository;

    public ResponseEntity<?> getHistory() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ResponseEntity<>(historyOrdersRepository.findAllByUserId(user.getId()), HttpStatus.OK);
    }

    public ResponseEntity<?> addReview(Review review, Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HistoryOrders historyOrders = historyOrdersRepository.findHistoryOrdersById(id);
        if (historyOrders == null)
            return new ResponseEntity<>(Map.of("message", "Отзыв можно оставить только после покупки."), HttpStatus.CONFLICT);
        Product product = productRepository.findProductById(historyOrders.getPurchaseProduct().getProductId());
        if (!reviewRepository.findReviewByUserIdAndProductId(user.getId(), product.getId()).isEmpty())
            return new ResponseEntity<>(Map.of("message", "Вы уже оставляли отзыв на этот товар."), HttpStatus.CONFLICT);
        product.getReviews().add(review);
        product.setGrade(gradeLogic(product.getReviews()));
        review.setUser(user);
        review.setProduct(product);
        reviewRepository.save(review);
        productRepository.save(product);
        return new ResponseEntity<>(Map.of("message", "Отзыв оставлен!"), HttpStatus.OK);
    }

    public ResponseEntity<?> returnProduct(Long id) {
        HistoryOrders historyOrders = historyOrdersRepository.findHistoryOrdersById(id);
        if (historyOrders == null)
            return new ResponseEntity<>(Map.of("message", "Товара нет"), HttpStatus.CONFLICT);
        if (Duration.between(LocalDateTime.now(), historyOrders.getDate()).toDays() > 1) {
            return new ResponseEntity<>(Map.of("message", "Возврат запрещен"), HttpStatus.OK);
        }
        Product product = productRepository.findProductById(historyOrders.getPurchaseProduct().getProductId());
        product.setQuantityInStock(product.getQuantityInStock() + 1);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        user.setBalance(user.getBalance() + historyOrders.getPurchaseAmount());
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
        if (product.getOrganization() == null)
            return new ResponseEntity<>(Map.of("message", "Организация удалена"), HttpStatus.NOT_FOUND);
        if (product.getOrganization().isFrozen())
            return new ResponseEntity<>(Map.of("message", "Организация заморожена."), HttpStatus.NO_CONTENT);
        if (product.getQuantityInStock().equals(0))
            return new ResponseEntity<>(Map.of("message", "Товара нет в наличии!"), HttpStatus.NO_CONTENT);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getBalance() < product.getPrice())
            return new ResponseEntity<>(Map.of("message", "Недостаточно средств."), HttpStatus.CONFLICT);
        product.setQuantityInStock(product.getQuantityInStock() - 1);
        user.setBalance(user.getBalance() - product.getPrice());
        Organization organization = organizationRepository.findOrganizationById(product.getOrganization().getId());
        organization.setBalance(organization.getBalance() + (product.getPrice() * 0.13));
        PurchaseProduct purchaseProduct = new PurchaseProduct(product);
        purchaseProductRepository.save(purchaseProduct);
        organizationRepository.save(organization);
        userRepository.save(user);
        productRepository.save(product);
        HistoryOrders historyOrders = new HistoryOrders();
        historyOrders.setUser(user);
        historyOrders.setPurchaseAmount(product.getPrice());
        historyOrders.setPurchaseProduct(purchaseProduct);
        historyOrdersRepository.save(historyOrders);
        return new ResponseEntity<>(Map.of("message", "Поздравляем с покупкой!"), HttpStatus.OK);
    }

    private Double gradeLogic(List<Review> list) {
        double grade = 0;
        for (Review review : list) {
            grade += review.getGrade();
        }
        return grade / list.size();
    }

    public ResponseEntity<?> allNotificationsForUser(Long id) {
        return new ResponseEntity<>(notificationRepository.findAllByUserId(id), HttpStatus.OK);
    }

    public ResponseEntity<?> registerOrganization(OrganizationConsideration organization) {
        if (organizationRepository.findByName(organization.getName()) != null)
            return new ResponseEntity<>(Map.of("message", "Организация с такими названием зарегистрирована"), HttpStatus.CONFLICT);
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        organization.setUser(user);
        organization.setFrozen(true);
        organizationConsiderationRepository.save(organization);
        return new ResponseEntity<>(Map.of("message", "Заявка на рассмотрении"), HttpStatus.OK);
    }
}
