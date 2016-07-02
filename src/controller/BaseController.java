package controller;

import model.*;
import org.springframework.beans.propertyeditors.*;
import org.springframework.context.*;
import org.springframework.context.support.*;
import org.springframework.jdbc.core.*;
import org.springframework.validation.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;

public class BaseController {
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public JdbcTemplate getJdbcTemplate() {
        ApplicationContext context = new ClassPathXmlApplicationContext("ProjectJDBC.xml");
        ProjectJDBC projectJDBC = (ProjectJDBC) context.getBean("ProjectJDBC");
        JdbcTemplate jdbcTemplate = projectJDBC.jdbcTemplate;
        return jdbcTemplate;
    }

    public JTableResult toError(BindingResult bindingResult) {
        JTableResult rslt = new JTableResult();
        ObjectError objectError = bindingResult.getAllErrors().get(0);
        rslt.Result = "Error";
        String message = objectError.getDefaultMessage();
        rslt.Message = message;
        if (objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            String fieldName = fieldError.getField();
            rslt.Message = fieldName + " " + message;
            String[] codes = fieldError.getCodes();
            if (codes != null && codes.length > 2) {
                if ("typeMismatch.java.util.Date".equals(codes[2])) {
                    rslt.Message = fieldName + " is Invalid date format";
                }
            }
        }
        return rslt;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
}
