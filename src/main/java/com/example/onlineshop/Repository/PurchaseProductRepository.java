package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.PurchaseProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseProductRepository extends CrudRepository<PurchaseProduct, Long> {
}
