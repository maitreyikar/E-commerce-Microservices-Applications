package com.mymart.orderservice.repository;

import com.mymart.orderservice.model.CartItem;
import com.mymart.orderservice.model.CartItemPrimaryKey;
import org.springframework.data.repository.CrudRepository;
import java.util.List;


public interface CartItemRepository extends CrudRepository<CartItem, CartItemPrimaryKey>{
    List<CartItem> findByUserId(Long user_id);
}
