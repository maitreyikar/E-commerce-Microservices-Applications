package com.mymart.orderservice.controller;


import com.mymart.orderservice.model.Product;
import com.mymart.orderservice.model.User;
import com.mymart.orderservice.model.OrderDTO;
import com.mymart.orderservice.model.Order;
import com.mymart.orderservice.model.OrderDetails;
import com.mymart.orderservice.repository.OrderRepository;
import com.mymart.orderservice.repository.OrderDetailsRepository;


import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.time.LocalDate; 
import java.time.LocalTime;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;





@Controller
public class OrderController {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EntityManager entityManager;

    public OrderController(OrderRepository orderRepository, OrderDetailsRepository orderDetailsRepository){
        this.orderRepository = orderRepository;
        this.orderDetailsRepository = orderDetailsRepository;
    }
    

    @PostMapping("/order/confirm")
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO order) {
        // Process the order using the cartItems received
        Map<Long, Integer> cart = order.getCartItems(); 
        Map<Product, Integer> cartItems = new HashMap<>();
        

        double cartTotal = 0;
        for (Map.Entry<Long, Integer> entry : cart.entrySet()) {
            
            String url = UriComponentsBuilder.fromHttpUrl("http://product-service:8080")  //change this to localhost for local deployment
                    .path("/products/fetch/" + String.valueOf(entry.getKey())) // Endpoint in product-service controller
                    .toUriString();
            
            Product product = (Product)restTemplate.getForObject(url, Product.class);
            cartItems.put(product, entry.getValue());
            cartTotal = cartTotal + entry.getValue() * product.getPrice();
        }

        
        Order newOrder = new Order();
        newOrder.setAddress(order.getAddress());

        User user = entityManager.find(User.class, order.getUser().getId());

        newOrder.setUser(user);
        newOrder.setTotal(cartTotal);
        newOrder.setTime(LocalTime.now());
        newOrder.setDate(LocalDate.now());
        orderRepository.save(newOrder);



        for (Map.Entry<Product, Integer> entry : cartItems.entrySet()) {
            OrderDetails details = new OrderDetails();
            details.setOrder(newOrder);

            Product product = entityManager.find(Product.class, entry.getKey().getId());

            details.setProduct(product);
            details.setQuantity(entry.getValue());
            orderDetailsRepository.save(details);
        }
        
        return ResponseEntity.ok("Order created successfully!");

    }

    @GetMapping("/order/history/{user_id}")
    public @ResponseBody List<Order> getOrders(@PathVariable Long user_id) {
        List<Order> userOrders = orderRepository.findByUserId(user_id);
        return userOrders;
    }

    @GetMapping("/order/history/{order_id}/items")
    public @ResponseBody Map<Long, Integer> getOrderItems(@PathVariable Long order_id) {
        List<OrderDetails> orderItems = orderDetailsRepository.findByOrderId(order_id);
        Map<Long, Integer> items = new HashMap<>();

        for(OrderDetails item: orderItems){
            items.put((item.getProduct()).getId(), item.getQuantity());
        }
        return items;
    }
    


}
