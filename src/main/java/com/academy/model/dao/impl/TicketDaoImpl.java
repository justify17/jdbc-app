package com.academy.model.dao.impl;

import com.academy.DataSource;
import com.academy.model.dao.TicketDao;
import com.academy.model.entity.Order;
import com.academy.model.entity.Route;
import com.academy.model.entity.Ticket;
import com.academy.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDaoImpl implements TicketDao {

    @Override
    public void create(Ticket entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "insert into ticket(id_route,id_order,passport_data) values(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entity.getRoute().getId());
            preparedStatement.setInt(2, entity.getOrder().getId());
            preparedStatement.setString(3, entity.getPassportData());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Ticket entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "update ticket set id_route=?,id_order=?,passport_data=? where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entity.getRoute().getId());
            preparedStatement.setInt(2, entity.getOrder().getId());
            preparedStatement.setString(3, entity.getPassportData());
            preparedStatement.setInt(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Ticket entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "delete from ticket where id=" + entity.getId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Ticket> getAll() {
        List<Ticket> tickets = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select ticket.id,ticket.id_route,route.id_departure,route.id_arrival,ticket.id_order," +
                " airport.order.number,airport.order.order_date,airport.order.id_user,ticket.passport_data from ticket" +
                " inner join route on ticket.id_route=route.id" +
                " inner join airport.order on ticket.id_order=airport.order.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Ticket ticket = new Ticket();
                ticket.setId(resultSet.getInt(1));
                Route route = new Route();
                route.setId(resultSet.getInt(2));
                route.setIdDeparture(resultSet.getInt(3));
                route.setIdArrival(resultSet.getInt(4));
                ticket.setRoute(route);
                Order order = new Order();
                order.setId(resultSet.getInt(5));
                order.setNumber(resultSet.getInt(6));
                order.setOrderDate(resultSet.getString(7));
                User user = new User();
                user.setId(resultSet.getInt(8));
                order.setUser(user);
                ticket.setOrder(order);
                ticket.setPassportData(resultSet.getString(9));
                tickets.add(ticket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tickets;
    }

    @Override
    public Ticket getById(int id) {
        Ticket ticket = new Ticket();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select ticket.id,ticket.id_route,route.id_departure,route.id_arrival,ticket.id_order," +
                " airport.order.number,airport.order.order_date,airport.order.id_user,ticket.passport_data from ticket" +
                " inner join route on ticket.id_route=route.id" +
                " inner join airport.order on ticket.id_order=airport.order.id where ticket.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            ticket.setId(resultSet.getInt(1));
            Route route = new Route();
            route.setId(resultSet.getInt(2));
            route.setIdDeparture(resultSet.getInt(3));
            route.setIdArrival(resultSet.getInt(4));
            ticket.setRoute(route);
            Order order = new Order();
            order.setId(resultSet.getInt(5));
            order.setNumber(resultSet.getInt(6));
            order.setOrderDate(resultSet.getString(7));
            User user = new User();
            user.setId(resultSet.getInt(8));
            order.setUser(user);
            ticket.setOrder(order);
            ticket.setPassportData(resultSet.getString(9));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ticket;
    }
}
