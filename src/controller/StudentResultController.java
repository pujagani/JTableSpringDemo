package controller;

import model.*;
import model.StudentResult;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/StudentResult")
public class StudentResultController extends BaseController {
    @ResponseBody
    @RequestMapping(value = "List")
    public JTableResult List(JTableRequest jTableRequest, int student_id) {
        JTableResult rslt = new JTableResult();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            return StudentResult.retrievePage(jdbcTemplate, student_id, jTableRequest);
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Save")
    public JTableResult Save(HttpServletRequest request, @Valid StudentResult studentResult, BindingResult bindingResult) {
        JTableResult rslt = new JTableResult();
        if (bindingResult.hasErrors()) return toError(bindingResult);

        int action = Integer.parseInt(request.getParameter("action"));
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            if (action == 1) studentResult.insert(jdbcTemplate);
            else studentResult.update(jdbcTemplate);
            studentResult = StudentResult.retrieveById(jdbcTemplate, studentResult.id);
            rslt.Result = "OK";
            rslt.Record = studentResult;
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
            StudentResult.delete(jdbcTemplate, id);
            rslt.Result = "OK";
            return rslt;
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }
}
