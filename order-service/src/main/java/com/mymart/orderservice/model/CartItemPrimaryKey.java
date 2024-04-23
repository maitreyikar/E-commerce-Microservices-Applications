package com.mymart.orderservice.model;

import java.io.Serializable;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemPrimaryKey implements Serializable{
    private User user_id;
    private Product product_id;
}
