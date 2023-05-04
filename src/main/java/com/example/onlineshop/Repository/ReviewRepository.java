package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Long> {
    Review findReviewByUserId(Long id);
}
