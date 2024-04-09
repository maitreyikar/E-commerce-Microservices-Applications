package com.mymart.userservice.model;

//import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class Order{
    private Long id;
   
    private User user;

    private double total;

    private String address; 

    private LocalDate date;
    
    private LocalTime time;

}
