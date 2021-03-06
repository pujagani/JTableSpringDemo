package model;

import com.fasterxml.jackson.databind.annotation.*;
import controller.*;
import org.hibernate.validator.constraints.*;
import org.springframework.format.annotation.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;

import javax.validation.constraints.*;
import java.sql.*;
import java.util.Date;
import java.util.*;

public class StudentPhone {
    public static String dropSQL = "Drop table student_phone If Exists Cascade";
    public static String createSQL = "Create table student_phone (" +
            "  id Integer Generated By Default As Identity(Start With 1) primary key," +
            "  student_id Integer Not Null," +
            "  phone_type Integer Not Null," +
            "  phone_number Varchar(20)," +
            "  record_date Date" +
            ");";
    public static String alterSQL = "Alter table student_phone Add Constraint student_phone_student_fk Foreign Key (student_id) References student (id)";
    public int id;
    public int student_id;
    @NotNull
    public int phone_type;
    @NotNull
    @Size(min = 8, max = 20)
    public String phone_number;
    @DateTimeFormat(pattern = BaseController.DATE_FORMAT)
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date record_date;

    public static JTableResult retrievePage(JdbcTemplate jdbcTemplate, int student_id, JTableRequest jTableRequest) {
        String orderBy = "phone_type";
        if (jTableRequest.jtSorting != null) orderBy = jTableRequest.jtSorting;
        String sql = "Select id, student_id, phone_type, phone_number, record_date From student_phone Where student_id = " + student_id +
                " Order By " + orderBy + " Offset " + jTableRequest.jtStartIndex + " Limit " + jTableRequest.jtPageSize;
        List<StudentPhone> rows = jdbcTemplate.query(sql, new StudentPhone.RowMaper());

        JTableResult rslt = new JTableResult();
        rslt.Result = "OK";
        rslt.Records = rows;
        rslt.TotalRecordCount = jdbcTemplate.queryForObject("Select count(*) From student_phone Where student_id = " + student_id, Integer.class);

        return rslt;
    }

    public static StudentPhone retrieveById(JdbcTemplate jdbcTemplate, int id) {
        String sql = "Select id, student_id, phone_type, phone_number, record_date From student_phone" +
                " Where id = ?";
        StudentPhone studentPhone = jdbcTemplate.queryForObject(sql, new Object[]{id}, new StudentPhone.RowMaper());
        return studentPhone;
    }

    public static void delete(JdbcTemplate jdbcTemplate, int id) {
        String sql = "Delete From student_phone Where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void insert(JdbcTemplate jdbcTemplate) {
        String sql = "Insert into student_phone(student_id, phone_type, phone_number, record_date)" +
                " Values(?, ?, ?, ?)";
        if (record_date == null) record_date = new Date();
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, student_id);
                ps.setInt(2, phone_type);
                ps.setString(3, phone_number);
                ps.setDate(4, new java.sql.Date(record_date.getTime()));
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, holder);
        id = holder.getKey().intValue();
    }

    public void update(JdbcTemplate jdbcTemplate) {
        String sql = "Update student_phone Set phone_type = ?, phone_number = ?" +
                " Where id = ?";
        jdbcTemplate.update(sql, phone_type, phone_number, id);
    }

    public static class RowMaper implements RowMapper<StudentPhone> {
        public StudentPhone mapRow(ResultSet rs, int rowNum) throws SQLException {
            StudentPhone Student = new StudentPhone();
            Student.id = rs.getInt(1);
            Student.student_id = rs.getInt(2);
            Student.phone_type = rs.getInt(3);
            Student.phone_number = rs.getString(4);
            Student.record_date = rs.getDate(5);
            return Student;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public int getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(int phone_type) {
        this.phone_type = phone_type;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Date getRecord_date() {
        return record_date;
    }

    public void setRecord_date(Date record_date) {
        this.record_date = record_date;
    }
}
