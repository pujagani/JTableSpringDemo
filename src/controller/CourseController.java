package controller;

import model.Course;
import model.JTableRequest;
import model.JTableResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/Course")
public class CourseController extends BaseController {

    @RequestMapping(value = "")
    public String index() {
        return "/jsp/Course.jsp";
    }

    @ResponseBody
    @RequestMapping(value = "List")
    public JTableResult List(JTableRequest jTableRequest) {
        JTableResult rslt = new JTableResult();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            return Course.retrievePage(jdbcTemplate, jTableRequest);
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Save")
    public JTableResult Save(HttpServletRequest request, @Valid @ModelAttribute("Course") Course course, BindingResult bindingResult) {
        JTableResult rslt = new JTableResult();
        if (bindingResult.hasErrors()) return toError(bindingResult);
        int action = Integer.parseInt(request.getParameter("action"));

        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            if (action == 1) course.insert(jdbcTemplate);
            else course.update(jdbcTemplate);
            rslt.Result = "OK";
            rslt.Record = course;
            return rslt;
        } catch (Exception ex) {
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
            Course.delete(jdbcTemplate, id);
            rslt.Result = "OK";
            return rslt;
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Retrieve")
    public Course Retrieve(int id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        return Course.retrieveById(jdbcTemplate, id);
    }

}