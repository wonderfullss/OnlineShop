package com.example.onlineshop.Repository;

import com.example.onlineshop.Entity.HistoryOrders;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryOrdersRepository extends CrudRepository<HistoryOrders, Long> {
    List<HistoryOrders> findAllByUserId(Long id);

    HistoryOrders findHistoryOrdersById(Long id);
}
