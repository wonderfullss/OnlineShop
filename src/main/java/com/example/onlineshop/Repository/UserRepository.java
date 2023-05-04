package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);

    User findUserById(Long id);

    User findUserByEmail(String email);
}