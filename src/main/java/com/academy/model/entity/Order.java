package com.academy.model.entity;

import lombok.Data;

@Data
public class Order {
    private int id;
    private int number;
    private String orderDate;
    private User user;
}
