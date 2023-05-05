package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {
    @Query("FROM Product where isFrozen = false")
    List<Product> findAllProduct();

    List<Product> findAllByOrganizationId(Long id);

    Product findProductById(Long id);
}
