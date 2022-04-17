package com.academy.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class Airplane {
    private int id;
    private Aircompany aircompany;
    private List<Route> routes;
}
