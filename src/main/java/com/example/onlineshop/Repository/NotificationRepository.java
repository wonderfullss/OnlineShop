package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Notification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    List<Notification> findAllByUserId(Long id);
}
