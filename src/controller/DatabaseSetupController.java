package controller;

import com.fasterxml.jackson.databind.*;
import dbsetup.*;
import model.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping(value = "/DatabaseSetup")
public class DatabaseSetupController {
    @RequestMapping(value = "")
    public String index(ModelMap model, String table) {
        model.addAttribute("Error", "");
        try {
            ApplicationContext context = new ClassPathXmlApplicationContext("ProjectJDBC.xml");
            ProjectJDBC projectJDBC = (ProjectJDBC) context.getBean("ProjectJDBC");
            JdbcTemplate jdbcTemplate = projectJDBC.jdbcTemplate;

            if ("city".equals(table)) processCity(jdbcTemplate);
            else if ("course".equals(table)) processCourse(jdbcTemplate);
            else if ("student".equals(table)) processStudent(jdbcTemplate);
            else if ("student_phone".equals(table)) processStudentPhone(jdbcTemplate);
            else if ("student_result".equals(table)) processStudentResult(jdbcTemplate);

            List<String[]> rslt = getRowCount(jdbcTemplate);
            ObjectMapper mapper = new ObjectMapper();
            String rowCount = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rslt);
            model.addAttribute("RowCount", rowCount);
        } catch (Throwable ex) {
            String msg = ex.getMessage();
            model.addAttribute("Error", ex.getMessage());
        }

        return "/jsp/DatabaseSetup.jsp";
    }

    public List<String[]> getRowCount(JdbcTemplate jdbcTemplate) {
        List<String[]> rslt = new ArrayList<>();
        getRowCount(jdbcTemplate, rslt, "city");
        getRowCount(jdbcTemplate, rslt, "course");
        getRowCount(jdbcTemplate, rslt, "student");
        getRowCount(jdbcTemplate, rslt, "student_phone");
        getRowCount(jdbcTemplate, rslt, "student_result");
        return rslt;
    }

    public void getRowCount(JdbcTemplate jdbcTemplate, List<String[]> rslt, String table) {
        int count = 0;
        try {
            count = jdbcTemplate.queryForObject("Select count(*) From " + table, Integer.class);
            rslt.add(new String[]{table, count + ""});
        } catch (Exception ex) {
            rslt.add(new String[]{table, ex.getMessage()});
        }
    }

    public void processCity(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(City.dropSQL);
        jdbcTemplate.execute(City.createSQL);

        String sql = "Insert into city(name) values (?)";
        for (String city : DataRepo.city_data) {
            jdbcTemplate.update(sql, city);
        }
    }

    public void processCourse(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(Course.dropSQL);
        jdbcTemplate.execute(Course.createSQL);

        String sql = "Insert into course(name) values (?)";
        for (String course : DataRepo.course_data) {
            jdbcTemplate.update(sql, course);
        }
    }

    public void processStudent(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(Student.dropSQL);
        jdbcTemplate.execute(Student.createSQL);
        jdbcTemplate.execute(Student.alterSQL);

        for (int i = 0; i < 200; i++) {
            Student student = new Student();
            int pos = (int) (Math.random() * DataRepo.person_data.length);
            int pos2 = (int) (Math.random() * DataRepo.person_data.length);
            student.name = DataRepo.person_data[pos][0] + " " + DataRepo.person_data[pos2][1];
            student.email = DataRepo.person_data[pos][0] + "." + DataRepo.person_data[pos2][1] + "@gmail.com";
            student.password = "Prescient#1";
            student.gender = DataRepo.person_data[pos][2];
            int cty = (int) (Math.random() * DataRepo.city_data.length);
            student.city_id = cty + 1;
            student.birth_date = new Date(100 - (int) (Math.random() * 20), (int) (Math.random() * 11), (int) (Math.random() * 30) + 1);
            student.education = (int) (Math.random() * 3) + 1;
            student.about = "Studies at UCLA";
            student.active_flg = "Y";
            student.record_date = new Date(115 - (int) (Math.random() * 2), (int) (Math.random() * 11), (int) (Math.random() * 30) + 1);
            student.insert(jdbcTemplate);
        }
    }

    public void processStudentPhone(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(StudentPhone.dropSQL);
        jdbcTemplate.execute(StudentPhone.createSQL);
        jdbcTemplate.execute(StudentPhone.alterSQL);
    }

    public void processStudentResult(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute(StudentResult.dropSQL);
        jdbcTemplate.execute(StudentResult.createSQL);
        jdbcTemplate.execute(StudentResult.alterSQL);
    }
}