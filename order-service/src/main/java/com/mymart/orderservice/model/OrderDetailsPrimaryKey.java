package com.mymart.orderservice.model;

import java.io.Serializable;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailsPrimaryKey implements Serializable{
    private Order order;
    private Product product;
}
