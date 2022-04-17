package com.academy.model.dao.impl;

import com.academy.DataSource;
import com.academy.model.dao.RoleDao;
import com.academy.model.entity.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoleDaoImpl implements RoleDao {

    @Override
    public void create(Role entity) {
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "insert into role() values()";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Role entity) {

    }

    @Override
    public void delete(Role entity) {
        Connection connection = DataSource.getInstance().getConnection();
        try (Statement statement = connection.createStatement()) {
            String sql = "delete from role where id=" + entity.getId();
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Role> getAll() {
        List<Role> roles = new ArrayList<>();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select * from role";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Role role = new Role();
                role.setId(resultSet.getInt(1));
                roles.add(role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    @Override
    public Role getById(int id) {
        Role role = new Role();
        Connection connection = DataSource.getInstance().getConnection();
        String sql = "select * from role where id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            role.setId(resultSet.getInt(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return role;
    }
}
