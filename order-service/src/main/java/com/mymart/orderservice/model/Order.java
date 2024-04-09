package com.mymart.orderservice.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "orders")
public class Order{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private double total;

    private String address; 

    private LocalDate date;
    
    private LocalTime time;

    public Long getId(){
        return id;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public void setTotal(double total){
        this.total = total;
    }

    public double getTotal(){
        return total;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setTime(LocalTime time){
        this.time = time;
    }

    public LocalTime getTime(){
        return time;
    }

}
