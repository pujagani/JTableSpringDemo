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
@RequestMapping(value = "/StudentPhone")
public class StudentPhoneController extends BaseController {
    @ResponseBody
    @RequestMapping(value = "List")
    public JTableResult List(JTableRequest jTableRequest, int student_id) {
        JTableResult rslt = new JTableResult();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            return StudentPhone.retrievePage(jdbcTemplate, student_id, jTableRequest);
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Save")
    public JTableResult Save(HttpServletRequest request, @Valid StudentPhone studentPhone, BindingResult bindingResult) {
        JTableResult rslt = new JTableResult();
        if (bindingResult.hasErrors()) return toError(bindingResult);

        int action = Integer.parseInt(request.getParameter("action"));
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            if (action == 1) studentPhone.insert(jdbcTemplate);
            else studentPhone.update(jdbcTemplate);
            studentPhone = StudentPhone.retrieveById(jdbcTemplate, studentPhone.id);
            rslt.Result = "OK";
            rslt.Record = studentPhone;
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
            StudentPhone.delete(jdbcTemplate, id);
            rslt.Result = "OK";
            return rslt;
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }
}
