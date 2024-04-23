package com.mymart.userservice.model;

import lombok.Data;

@Data
public class CartItem {
    private User userId;
    private Product productId;
    private int quantity; 
}
