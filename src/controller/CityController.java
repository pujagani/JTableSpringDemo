package controller;

import com.fasterxml.jackson.databind.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.jdbc.core.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;
import model.*;

import javax.servlet.http.*;
import javax.validation.*;
import java.util.*;

@Controller
@RequestMapping(value = "/City")
public class CityController extends BaseController {

    @RequestMapping(value = "")
    public String index() {
        return "/jsp/City.jsp";
    }

    @ResponseBody
    @RequestMapping(value = "List")
    public JTableResult List(JTableRequest jTableRequest) {
        JTableResult rslt = new JTableResult();
        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            return City.retrievePage(jdbcTemplate, jTableRequest);
        } catch (Exception ex) {
            rslt.Result = "Error";
            rslt.Message = ex.getMessage();
            return rslt;
        }
    }

    @ResponseBody
    @RequestMapping(value = "Save")
    public JTableResult Save(HttpServletRequest request, @Valid @ModelAttribute("City") City city, BindingResult bindingResult) {
        JTableResult rslt = new JTableResult();
        if (bindingResult.hasErrors()) return toError(bindingResult);
        int action = Integer.parseInt(request.getParameter("action"));

        try {
            JdbcTemplate jdbcTemplate = getJdbcTemplate();
            if (action == 1) city.insert(jdbcTemplate);
            else city.update(jdbcTemplate);
            rslt.Result = "OK";
            rslt.Record = city;
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
            City.delete(jdbcTemplate, id);
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
    public City Retrieve(int id) {
        JdbcTemplate jdbcTemplate = getJdbcTemplate();
        return City.retrieveById(jdbcTemplate, id);
    }

}