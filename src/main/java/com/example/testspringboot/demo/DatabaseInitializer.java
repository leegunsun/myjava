package com.example.testspringboot.demo;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DatabaseInitializer {
    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate,DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @PostConstruct
    public  void init() throws SQLException {
        if (!tableExists("User")) { // "User"는 확인하고자 하는 테이블 이름
            createTable();
        }
    }

    private boolean tableExists(String tableName) throws SQLException {
        try (var connection = dataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet resultSet = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            return resultSet.next();
        }
    }

    public void createTable () {
        String sql = "CREATE TABLE USER (" +
                "id BIGINT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255), " +
                "email VARCHAR(255), " +
                "version INT)";
        jdbcTemplate.execute(sql);
    }

}
