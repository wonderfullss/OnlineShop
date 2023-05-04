package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findAllByOrganizationId(Long id);
    Product findProductById(Long id);
}
