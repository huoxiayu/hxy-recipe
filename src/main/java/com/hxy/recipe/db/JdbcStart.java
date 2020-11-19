package com.hxy.recipe.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
public class JdbcStart {

    public static void main(String[] args) throws SQLException {
        jdbcStart();
    }

    private static void jdbcStart() throws SQLException {
        Connection connection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/hxy",
                "root",
                "root"
        );

        PreparedStatement preparedStatement = connection.prepareStatement(
                "select id, name from student"
        );

        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            log.info("id {}, name {}", resultSet.getInt(0), resultSet.getString(1));
        }

        resultSet.close();
        preparedStatement.close();
        connection.close();
    }

}
