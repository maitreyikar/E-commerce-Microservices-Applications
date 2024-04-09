package com.mymart.orderservice.repository;

import com.mymart.orderservice.model.OrderDetails;
import com.mymart.orderservice.model.OrderDetailsPrimaryKey;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface OrderDetailsRepository extends CrudRepository<OrderDetails, OrderDetailsPrimaryKey>{
    List<OrderDetails> findByOrderId(Long orderId);
}
