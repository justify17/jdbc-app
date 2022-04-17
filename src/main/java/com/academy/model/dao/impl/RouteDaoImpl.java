package com.academy.model.dao.impl;

import com.academy.DataSource;
import com.academy.model.dao.RouteDao;
import com.academy.model.entity.Aircompany;
import com.academy.model.entity.Airplane;
import com.academy.model.entity.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {

    @Override
    public void create(Route entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "insert into route(id_departure,id_arrival) values(?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"})) {
            preparedStatement.setInt(1, entity.getIdDeparture());
            preparedStatement.setInt(2, entity.getIdArrival());
            preparedStatement.executeUpdate();
            if (entity.getAirplanes() != null) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int lastRouteId = resultSet.getInt(1);
                Connection connectionTwo = DataSource.getInstance().getConnection();
                sql = "insert into airplane_route(id_airplane,id_route) values (?,?)";
                try (PreparedStatement preparedStatementTwo = connectionTwo.prepareStatement(sql)) {
                    for (Airplane airplane : entity.getAirplanes()) {
                        preparedStatementTwo.setInt(1, airplane.getId());
                        preparedStatementTwo.setInt(2, lastRouteId);
                        preparedStatementTwo.executeUpdate();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Route entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "update route set id_departure=" + entity.getIdDeparture() +
                    ",id_arrival=" + entity.getIdArrival() +
                    " where id=" + entity.getId();
            statement.executeUpdate(sql);
            if (entity.getAirplanes() != null) {
                sql = "delete from airplane_route where id_route=" + entity.getId();
                statement.executeUpdate(sql);
                Connection connectionTwo = DataSource.getInstance().getConnection();
                sql = "insert into airplane_route(id_airplane,id_route) values (?,?)";
                try (PreparedStatement preparedStatement = connectionTwo.prepareStatement(sql)) {
                    for (Airplane airplane : entity.getAirplanes()) {
                        preparedStatement.setInt(1, airplane.getId());
                        preparedStatement.setInt(2, entity.getId());
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Route entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "delete from airplane_route where id_route=" + entity.getId();
            statement.addBatch(sql);
            sql = "delete from route where id=" + entity.getId();
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Route> getAll() {
        List<Route> routes = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        Connection connectionTwo = DataSource.getInstance().getConnection();
        String sqlAirplanes = "select id_airplane,airplane.id_aircompany" +
                " from airplane_route inner join airplane on airplane_route.id_airplane=airplane.id" +
                " where id_route=?";
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connectionTwo.prepareStatement(sqlAirplanes)) {
            String sqlRoutes = "select * from route";
            ResultSet resultSetRoutes = statement.executeQuery(sqlRoutes);
            while (resultSetRoutes.next()) {
                Route route = new Route();
                route.setId(resultSetRoutes.getInt(1));
                route.setIdArrival(resultSetRoutes.getInt(2));
                route.setIdDeparture(resultSetRoutes.getInt(3));
                List<Airplane> airplanes = new ArrayList<>();
                preparedStatement.setInt(1, resultSetRoutes.getInt(1));
                ResultSet resultSetAirplanes = preparedStatement.executeQuery();
                while (resultSetAirplanes.next()) {
                    Airplane airplane = new Airplane();
                    airplane.setId(resultSetAirplanes.getInt(1));
                    Aircompany aircompany = new Aircompany();
                    aircompany.setId(resultSetAirplanes.getInt(2));
                    airplane.setAircompany(aircompany);
                    airplanes.add(airplane);
                }
                route.setAirplanes(airplanes);
                routes.add(route);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    public Route getById(int id) {
        Route route = new Route();
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "select * from route where id=" + id;
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            route.setId(resultSet.getInt(1));
            route.setIdArrival(resultSet.getInt(2));
            route.setIdDeparture(resultSet.getInt(3));
            List<Airplane> airplanes = new ArrayList<>();
            sql = "select id_airplane,airplane.id_aircompany" +
                    " from airplane_route inner join airplane on airplane_route.id_airplane=airplane.id" +
                    " where id_route=" + id;
            ResultSet resultSetAirplanes = statement.executeQuery(sql);
            while (resultSetAirplanes.next()) {
                Airplane airplane = new Airplane();
                airplane.setId(resultSetAirplanes.getInt(1));
                Aircompany aircompany = new Aircompany();
                aircompany.setId(resultSetAirplanes.getInt(2));
                airplane.setAircompany(aircompany);
                airplanes.add(airplane);
            }
            route.setAirplanes(airplanes);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return route;
    }
}
