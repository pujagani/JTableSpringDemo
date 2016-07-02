package model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import controller.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.Date;
import java.util.List;

public class StudentResult {
    public static String dropSQL = "Drop table student_result If Exists Cascade";
    public static String createSQL = "Create table student_result (" +
            "  id Integer Generated By Default As Identity(Start With 1) primary key," +
            "  student_id Integer Not Null," +
            "  course_name Varchar (50)," +
            "  exam_date Date," +
            "degree Varchar (2)" +
            ");";
    public static String alterSQL = "Alter table student_result Add Constraint student_result_student_fk Foreign Key (student_id) References student (id)";
    public int id;
    public int student_id;
    public String course_name;
    @DateTimeFormat(pattern = BaseController.DATE_FORMAT)
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date exam_date;
    public String degree;


    public static JTableResult retrievePage(JdbcTemplate jdbcTemplate, int student_id, JTableRequest jTableRequest) {
        String orderBy = "course_name";
        if (jTableRequest.jtSorting != null) orderBy = jTableRequest.jtSorting;
        String sql = "Select id, student_id, course_name, exam_date,degree From student_result Where student_id = " + student_id +
                " Order By " + orderBy + " Offset " + jTableRequest.jtStartIndex + " Limit " + jTableRequest.jtPageSize;
        List<StudentResult> rows = jdbcTemplate.query(sql, new StudentResult.RowMaper());

        JTableResult rslt = new JTableResult();
        rslt.Result = "OK";
        rslt.Records = rows;
        rslt.TotalRecordCount = jdbcTemplate.queryForObject("Select count(*) From student_result Where student_id = " + student_id, Integer.class);

        return rslt;
    }

    public static StudentResult retrieveById(JdbcTemplate jdbcTemplate, int id) {
        String sql = "Select id, student_id, course_name, exam_date,degree  From student_result" +
                " Where id = ?";
        StudentResult studentResult = jdbcTemplate.queryForObject(sql, new Object[]{id}, new StudentResult.RowMaper());
        return studentResult;
    }

    public static void delete(JdbcTemplate jdbcTemplate, int id) {
        String sql = "Delete From student_result Where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void insert(JdbcTemplate jdbcTemplate) {
        String sql = "Insert into student_result(student_id, course_name, exam_date,degree )" +
                " Values(?, ?, ?, ?)";
        if (exam_date == null) exam_date = new Date();
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(final Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, student_id);
                ps.setString(2, course_name);
                ps.setDate(3, new java.sql.Date(exam_date.getTime()));
                ps.setString(4, degree);
                return ps;
            }
        };
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, holder);
        id = holder.getKey().intValue();
    }

    public void update(JdbcTemplate jdbcTemplate) {
        String sql = "Update student_result Set course_name = ?, degree = ?" +
                " Where id = ?";
        jdbcTemplate.update(sql, course_name, degree, id);
    }

    public static class RowMaper implements RowMapper<StudentResult> {
        public StudentResult mapRow(ResultSet rs, int rowNum) throws SQLException {
            StudentResult studentResult = new StudentResult();
            studentResult.id = rs.getInt(1);
            studentResult.student_id = rs.getInt(2);
            studentResult.course_name = rs.getString(3);
            studentResult.exam_date = rs.getDate(4);
            studentResult.degree = rs.getString(5);
            return studentResult;
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

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public Date getExam_date() {
        return exam_date;
    }

    public void setExam_date(Date exam_date) {
        this.exam_date = exam_date;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }
}
