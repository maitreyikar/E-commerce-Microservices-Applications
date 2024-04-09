package com.mymart.orderservice.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Map<Long, Integer> cartItems;
    private String  address;
    private User user;
}
