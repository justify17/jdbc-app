package com.academy.model.entity;

import lombok.Data;

import java.util.List;

@Data
public class Route {
    private int id;
    private int idDeparture;
    private int idArrival;
    private List<Airplane> airplanes;
}
