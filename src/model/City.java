package model;

import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;

import javax.validation.constraints.*;
import java.sql.*;
import java.util.*;

public class City {
    public int id;
    @NotNull
    public String name;
    public static String dropSQL = "Drop table city If Exists Cascade";
    public static String createSQL = "Create table city (" +
            "  id Integer Generated By Default As Identity(Start With 1) primary key," +
            "  name Varchar(50) Not Null" +
            ");";

    public static HashMap<Integer, String> retrieveAll(JdbcTemplate jdbcTemplate) {
        HashMap<Integer, String> rslt = new HashMap<Integer, String>();
        String sql = "Select id, name From city";
        List<City> cities = jdbcTemplate.query(sql, new RowMaper());
        for (City city : cities) {
            rslt.put(city.id, city.name);
        }
        return rslt;
    }

    public static JTableResult retrievePage(JdbcTemplate jdbcTemplate, JTableRequest jTableRequest) {
        String orderBy = "name";
        if (jTableRequest.jtSorting != null) orderBy = jTableRequest.jtSorting;
        String sql = "Select id, name From city Order By " + orderBy + " Offset " + jTableRequest.jtStartIndex + " Limit " + jTableRequest.jtPageSize;
        List<City> cities = jdbcTemplate.query(sql, new RowMaper());

        JTableResult rslt = new JTableResult();
        rslt.Result = "OK";
        rslt.Records = cities;
        rslt.TotalRecordCount = jdbcTemplate.queryForObject("Select count(*) From city", Integer.class);

        return rslt;
    }

    public static City retrieveById(JdbcTemplate jdbcTemplate, int id) {
        String sql = "Select id, name From city" +
                " Where id = ?";
        City city = jdbcTemplate.queryForObject(sql, new Object[]{id}, new City.RowMaper());
        return city;
    }

    public static class RowMaper implements RowMapper<City> {
        public City mapRow(ResultSet rs, int rowNum) throws SQLException {
            City City = new City();
            City.id = rs.getInt(1);
            City.name = rs.getString(2);
            return City;
        }
    }

    public void insert(JdbcTemplate jdbcTemplate) {
        String sql = "Insert into city(name) Values(?)";
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, holder);
        id = holder.getKey().intValue();
    }

    public void update(JdbcTemplate jdbcTemplate) {
        String sql = "Update city Set name = ?" +
                " Where id = ?";
        jdbcTemplate.update(sql, name, id);
    }

    public static void delete(JdbcTemplate jdbcTemplate, int id) {
        String sql = "Delete From city Where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
