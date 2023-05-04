package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    List<Review> findReviewByUserIdAndProductId(Long userId, Long productId);
}
