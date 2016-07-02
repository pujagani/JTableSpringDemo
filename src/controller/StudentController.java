package controller;

import com.fasterxml.jackson.databind.*;
import model.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.util.*;

@Controller
@RequestMapping(value = "/Student")
public class StudentController extends BaseController {

    @RequestMapping(value = "")
    public String index(ModelMap model) {
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            HashMap<Integer, String> cities = City.retrieveAll(jdbcTemplate);
            ObjectMapper mapper = new ObjectMapper();
            String OptionsCity = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(cities);
            HashMap<Integer, String> courses = Course.retrieveAll(jdbcTemplate);
            ObjectMapper mapper1 = new ObjectMapper();
            String OptionsCourse = mapper1.writerWithDefaultPrettyPrinter().writeValueAsString(courses);
            model.addAttribute("OptionsCity", OptionsCity);
            model.addAttribute("OptionsCourse", OptionsCourse);
            model.addAttribute("Error", "");
        } catch (Exception ex) {
            model.addAttribute("OptionsCity", 0);
            model.addAttribute("OptionsCourse", 0);
            model.addAttribute("Error", ex.getMessage());
        }
        return "/jsp/Student.jsp";
    }

    @ResponseBody
    @RequestMapping(value = "List")
    public JTableResult List(JTableRequest jTableRequest) {
        JTableResult rslt = new JTableResult();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            return Student.retrievePage(jdbcTemplate, jTableRequest);
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Save")
    public JTableResult Save(HttpServletRequest request, @Valid Student student, BindingResult bindingResult) {
        JTableResult rslt = new JTableResult();
        if (bindingResult.hasErrors()) return toError(bindingResult);

        int action = Integer.parseInt(request.getParameter("action"));
        if (student.active_flg == null) student.active_flg = "N";
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            if (action == 1) student.insert(jdbcTemplate);
            else student.update(jdbcTemplate);
            student = Student.retrieveById(jdbcTemplate, student.id);
            rslt.Result = "OK";
            rslt.Record = student;
            return rslt;
        } catch (Throwable ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Delete")
    public JTableResult Delete(int id) {
        JTableResult rslt = new JTableResult();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            Student.delete(jdbcTemplate, id);
            rslt.Result = "OK";
            return rslt;
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }
}
