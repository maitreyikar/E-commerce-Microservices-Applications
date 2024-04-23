package com.mymart.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@IdClass(CartItemPrimaryKey.class)
@Table(name = "cart_item")
public class CartItem {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user_id;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product_id;

    private int quantity;
    
    @Override
    public String toString(){
        //return "{" + String.valueOf(this.user_id.getId()) + ", " + this.product_id.getBrand() + ", " + this.product.getName() + ", " + String.valueOf(this.quantity) + "}";
        return "{" + String.valueOf(this.user_id.getId()) + ", " + this.product_id.getBrand() + ", " + this.product_id.getName() + ", " + String.valueOf(this.quantity) + "}";
    }
}
