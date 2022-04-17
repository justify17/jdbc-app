package com.academy.model.dao.impl;

import com.academy.DataSource;
import com.academy.model.dao.AircompanyDao;
import com.academy.model.entity.Aircompany;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AircompanyDaoImpl implements AircompanyDao {

    @Override
    public void create(Aircompany entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "insert into aircompany() values()";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Aircompany entity) {

    }

    @Override
    public void delete(Aircompany entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "delete from aircompany where id=" + entity.getId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Aircompany> getAll() {
        List<Aircompany> aircompanies = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select * from aircompany";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Aircompany aircompany = new Aircompany();
                aircompany.setId(resultSet.getInt(1));
                aircompanies.add(aircompany);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircompanies;
    }

    @Override
    public Aircompany getById(int id) {
        Aircompany aircompany = new Aircompany();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select * from aircompany where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            aircompany.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return aircompany;
    }
}
