package com.mymart.orderservice.repository;

import com.mymart.orderservice.model.Order;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long>{
    List<Order> findByUserId(Long userId);
}
