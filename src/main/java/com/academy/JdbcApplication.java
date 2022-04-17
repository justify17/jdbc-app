package com.academy;

import com.academy.model.dao.AirplaneDao;
import com.academy.model.dao.impl.AirplaneDaoImpl;
import com.academy.model.entity.Airplane;

import java.util.List;

public class JdbcApplication {
    public static void main(String[] args) {
        AirplaneDao airplaneDao = new AirplaneDaoImpl();
        List<Airplane> airplaneList = airplaneDao.getAll();
        System.out.println();
    }
}
