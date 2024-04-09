package com.mymart.orderservice.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@IdClass(OrderDetailsPrimaryKey.class)
@Table(name = "order_details")
public class OrderDetails {
    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    private int quantity;
    
    @Override
    public String toString(){
        return "{" + String.valueOf(this.order.getId()) + ", " + this.product.getBrand() + ", " + this.product.getName() + ", " + String.valueOf(this.quantity) + "}";
    }
}
