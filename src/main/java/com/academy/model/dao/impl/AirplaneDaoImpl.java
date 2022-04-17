package com.academy.model.dao.impl;

import com.academy.DataSource;
import com.academy.model.dao.AirplaneDao;
import com.academy.model.entity.Aircompany;
import com.academy.model.entity.Airplane;
import com.academy.model.entity.Route;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AirplaneDaoImpl implements AirplaneDao {

    @Override
    public void create(Airplane entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "insert into airplane(id_aircompany) values(?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"})) {
            preparedStatement.setInt(1, entity.getAircompany().getId());
            preparedStatement.executeUpdate();
            if (entity.getRoutes() != null) {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                int lastAirplaneId = resultSet.getInt(1);
                Connection connectionTwo = DataSource.getInstance().getConnection();
                sql = "insert into airplane_route(id_airplane,id_route) values (?,?)";
                try (PreparedStatement preparedStatementTwo = connectionTwo.prepareStatement(sql)) {
                    for (Route route : entity.getRoutes()) {
                        preparedStatementTwo.setInt(1, lastAirplaneId);
                        preparedStatementTwo.setInt(2, route.getId());
                        preparedStatementTwo.executeUpdate();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void update(Airplane entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "update airplane set id_aircompany=" + entity.getAircompany().getId() +
                    " where id=" + entity.getId();
            statement.executeUpdate(sql);
            if (entity.getRoutes() != null) {
                sql = "delete from airplane_route where id_airplane=" + entity.getId();
                statement.executeUpdate(sql);
                Connection connectionTwo = DataSource.getInstance().getConnection();
                sql = "insert into airplane_route(id_airplane,id_route) values (?,?)";
                try (PreparedStatement preparedStatement = connectionTwo.prepareStatement(sql)) {
                    for (Route route : entity.getRoutes()) {
                        preparedStatement.setInt(1, entity.getId());
                        preparedStatement.setInt(2, route.getId());
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Airplane entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "delete from airplane_route where id_airplane=" + entity.getId();
            statement.addBatch(sql);
            sql = "delete from airplane where id=" + entity.getId();
            statement.addBatch(sql);
            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Airplane> getAll() {
        List<Airplane> airplanes = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        Connection connectionTwo = DataSource.getInstance().getConnection();
        String sqlRoutes = "select id_route,route.id_departure,route.id_arrival " +
                "from airplane_route inner join route on airplane_route.id_route=route.id " +
                "where id_airplane=?";
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connectionTwo.prepareStatement(sqlRoutes)) {
            String sqlAirplanes = "select * from airplane";
            ResultSet resultSetAirplanes = statement.executeQuery(sqlAirplanes);
            while (resultSetAirplanes.next()) {
                Airplane airplane = new Airplane();
                airplane.setId(resultSetAirplanes.getInt(1));
                Aircompany aircompany = new Aircompany();
                aircompany.setId(resultSetAirplanes.getInt(2));
                airplane.setAircompany(aircompany);
                List<Route> routes = new ArrayList<>();
                preparedStatement.setInt(1, resultSetAirplanes.getInt(1));
                ResultSet resultSetRoutes = preparedStatement.executeQuery();
                while (resultSetRoutes.next()) {
                    Route route = new Route();
                    route.setId(resultSetRoutes.getInt(1));
                    route.setIdDeparture(resultSetRoutes.getInt(2));
                    route.setIdArrival(resultSetRoutes.getInt(3));
                    routes.add(route);
                }
                airplane.setRoutes(routes);
                airplanes.add(airplane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airplanes;
    }

    @Override
    public Airplane getById(int id) {
        Airplane airplane = new Airplane();
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "select * from airplane where id=" + id;
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();
            airplane.setId(resultSet.getInt(1));
            Aircompany aircompany = new Aircompany();
            aircompany.setId(resultSet.getInt(2));
            airplane.setAircompany(aircompany);
            List<Route> routes = new ArrayList<>();
            sql = "select id_route,route.id_departure,route.id_arrival " +
                    "from airplane_route inner join route on airplane_route.id_route=route.id " +
                    "where id_airplane=" + id;
            ResultSet resultSetRoutes = statement.executeQuery(sql);
            while (resultSetRoutes.next()) {
                Route route = new Route();
                route.setId(resultSetRoutes.getInt(1));
                route.setIdDeparture(resultSetRoutes.getInt(2));
                route.setIdArrival(resultSetRoutes.getInt(3));
                routes.add(route);
            }
            airplane.setRoutes(routes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airplane;
    }

    @Override
    public List<Airplane> getByAircompanyId(int aircompanyId) {
        List<Airplane> airplanes = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        Connection connectionTwo = DataSource.getInstance().getConnection();
        String sqlRoutes = "select id_route,route.id_departure,route.id_arrival " +
                "from airplane_route inner join route on airplane_route.id_route=route.id " +
                "where id_airplane=?";
        try (Statement statement = connection.createStatement();
             PreparedStatement preparedStatement = connectionTwo.prepareStatement(sqlRoutes)) {
            String sqlAirplanes = "select id from airplane where id_aircompany=" + aircompanyId;
            ResultSet resultSetAirplanes = statement.executeQuery(sqlAirplanes);
            while (resultSetAirplanes.next()) {
                Airplane airplane = new Airplane();
                airplane.setId(resultSetAirplanes.getInt(1));
                Aircompany aircompany = new Aircompany();
                aircompany.setId(aircompanyId);
                airplane.setAircompany(aircompany);
                List<Route> routes = new ArrayList<>();
                preparedStatement.setInt(1, resultSetAirplanes.getInt(1));
                ResultSet resultSetRoutes = preparedStatement.executeQuery();
                while (resultSetRoutes.next()) {
                    Route route = new Route();
                    route.setId(resultSetRoutes.getInt(1));
                    route.setIdDeparture(resultSetRoutes.getInt(2));
                    route.setIdArrival(resultSetRoutes.getInt(3));
                    routes.add(route);
                }
                airplane.setRoutes(routes);
                airplanes.add(airplane);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airplanes;
    }
}
