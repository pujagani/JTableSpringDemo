package model;

import org.springframework.jdbc.core.*;

import javax.sql.*;

public class ProjectJDBC implements ProjectDAO {
    public DataSource dataSource;
    public JdbcTemplate jdbcTemplate;

    public void setDataSource(DataSource ds) {
        dataSource = ds;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
