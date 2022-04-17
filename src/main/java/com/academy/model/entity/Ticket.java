package com.academy.model.entity;

import lombok.Data;

@Data
public class Ticket {
    private int id;
    private Route route;
    private Order order;
    private String passportData;
}
