package com.academy.model.dao.impl;

import com.academy.DataSource;
import com.academy.model.dao.OrderDao;
import com.academy.model.entity.Order;
import com.academy.model.entity.Role;
import com.academy.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    @Override
    public void create(Order entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "insert into airport.order(number,order_date,id_user) values(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entity.getNumber());
            preparedStatement.setString(2, entity.getOrderDate());
            preparedStatement.setInt(3, entity.getUser().getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Order entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "update airport.order set number=?,order_date=?,id_user=? where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, entity.getNumber());
            preparedStatement.setString(2, entity.getOrderDate());
            preparedStatement.setInt(3, entity.getUser().getId());
            preparedStatement.setInt(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Order entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "delete from airport.order where id=" + entity.getId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Order> getAll() {
        List<Order> orders = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select airport.order.id,number,order_date,id_user,id_role from airport.order" +
                " inner join user on airport.order.id_user=user.id";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order order = new Order();
                order.setId(resultSet.getInt(1));
                order.setNumber(resultSet.getInt(2));
                order.setOrderDate(resultSet.getString(3));
                User user = new User();
                user.setId(resultSet.getInt(4));
                Role role = new Role();
                role.setId(resultSet.getInt(5));
                user.setRole(role);
                order.setUser(user);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order getById(int id) {
        Order order = new Order();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select airport.order.id,number,order_date,id_user,id_role from airport.order" +
                " inner join user on airport.order.id_user=user.id where airport.order.id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            order.setId(resultSet.getInt(1));
            order.setNumber(resultSet.getInt(2));
            order.setOrderDate(resultSet.getString(3));
            User user = new User();
            user.setId(resultSet.getInt(4));
            Role role = new Role();
            role.setId(resultSet.getInt(5));
            user.setRole(role);
            order.setUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }
}
