#JTable Spring Demo
######Contents:
- Introduction
- Model
- Controller
- DropDown Options
- View
- Form Validation
- Handling Date Format
- Themes
- Database Access Setup
- KeyHolder
- Artifacts
- Using other databases

######Introduction
Model–view–controller (MVC) is a software architecture pattern. Initially intended for desktop computing, in the recent years it is used for developing web applications. 

Model- Model is the data or objects that the application interacts with. 

View- View provides an interactive user interface to display the data provided by the model.

Controller- Controller is the module that handles interaction between view and model component. It processes the user request, calls the appropriate model and sends the data to be rendered to the view to display it to the user.

Java Spring MVC framework helps in developing loosely coupled applications using MVC architecture. Understanding and setup of basic Spring MVC Framework can be done by following this tutorial: http://www.tutorialspoint.com/spring/spring_web_mvc_framework.htm

Most applications require database access. User interface is provided by most applications to allow user to add, delete and update data. jTable provides a jQuery plugin to create interactive tables which allow the same functions of adding, deleting and updating data. It also has several features like: paging, sorting, create or edit record in dialog form, master/child tables etc. 
See http://jtable.org/ detailed documentation.  

The goal of this article is to show how jTable can be used easily in integration with Java Spring.

######Model
Two basic classes are Student and City. Student class represents a record in Student database table and City class represents a record in City database table. A student lives in a city and a list of cities is provided for the student to select. Student table has CityId which is the Id in the City table. 

Student Class is as follows:
```
public class Student {
    
    public int id;
    @NotNull
    @Size(min = 2, max = 30)
    public String name;
    @NotNull
    @Email
    public String email;
    @NotNull
    public String password;
    @NotNull
    public String gender;
    @NotNull
    public int city_id;
    @NotNull
    @DateTimeFormat(pattern = BaseController.DATE_FORMAT)
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date birth_date;
    @NotNull
    public int education;
    public String about;
    @NotNull
    public String active_flg;
    @DateTimeFormat(pattern = BaseController.DATE_FORMAT)
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date record_date;

}

```
City Class is as follows:
```
public class City {
    public int id;
    @NotNull
    public String name;
}
```
Similarly, classes are created for

-	StudentResults with fields id, student_id, course_name, exam_date,degree.
-	StudentPhone with fields id, student_id, phone_type, phone_number, record_date.
-	Course with fields id, name.
	
Getter and setter methods were added to all the classes. 
jTable allows master/child tables (http://jtable.org/Demo/MasterChild). StudentPhone and StudentResults are defined for child tables.

######Controller
Every class in the model has a respective controller. Controller defines the methods for listing, deleting and saving records. These methods make call to methods defined in the model for database access. The results returned are provided to the view for displaying. 
Student Controller:
List Method
```
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
```
RetrievePage method will return records based on the paging and sorting parameters. 

Save Method
```
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
```
Save method calls the insert or the update method of Student.java class based on the parameter value of action passed. Datatype error handling (explained later) bit is also taken care in this section.

Delete Method
```
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
```
Delete method of Student.java class is called and it deletes the student record of that particular student id.
Similarly, following controllers are created:
- CityController 
- CourseController
- StudentResultController
- StudentPhoneController
jTable expects the data in certain format. In order to implement that, JTableResult.java and JTableResponse.java was created with the expected fields. These fields are set as per jTable requirements. 

######Dropdown Options
For select, radio button, checkbox jTable supports code value pair data. 
Ex: ``` options: { '1': 'Home phone', '2': 'Office phone', '3': 'Cell phone' } ```
(See- http://jtable.org/ApiReference/FieldOptions#fopt-options)
For our Student example, we have City and Course as select dropdown. The key-value pairs for list of cities and courses are stored using a HashMap (retrieveAll method in City.java and Course. java ). Spring takes care of converting it correctly. 

######View
JavaScriptAndCSS.jsp includes all the necessary jTable and jQuery UI scripts. It also defines a menu, allowing a user to select the database he wants to view. 
Student.jsp includes the JavaScriptAndCSS.jsp file and obtains the options for cities and courses.
```
var JTableInfo =
       {
           title: 'The Student List',
           paging: true,
           pageSize: 10,
           sorting: true,
           defaultSorting: 'name ASC',
           actions: {
               listAction: 'Student/List',
               createAction: 'Student/Save?action=1',
               updateAction: 'Student/Save?action=2',
               deleteAction: 'Student/Delete'
           },
           fields: {
               id: {
                   key: true,
                   create: false,
                   edit: false,
                   list: false
               },
               phones: {
                   title: '',
                   width: '2%',
                   sorting: false,
                   edit: false,
                   create: false,
                   display: studentPhone
               },
                             
               password: {
                   title: 'User Password',
                   type: 'password',
                   list: false
               },
               gender: {
                   title: 'Gender',
                   width: '13%',
                   options: { 'M': 'Male', 'F': 'Female' }
               },
               city_id: {
                   title: 'City',
                   width: '12%',
                   options: cities
               },
               birth_date: {
                   title: 'Birth date',
                   width: '15%',
                   type: 'date',
               }
            }
       }

    $('#JTable').jtable(JTableInfo);
    $('#JTable').jtable('load');

});
```
JTableInfo sets the title of the table, actions for the records and fields of the record in the database.
Each field has set of properties for behaviour of that field. Above code snippet shows selected fields of the Student table (See Student.jsp for entire code). Similarly, view City.jsp and Course.jsp is created for City and Course respectively.
To display the child tables (StudentPhone and StudentResult), functions studentPhone() and studentResult() are defined in Student.jsp. See http://jtable.org/Demo/MasterChild for details. 

######Form Validation
Form validation is done by using annotations. 
Required field - ```@NotNull``` annotation indicates a compulsory field.
Size of the field- ```@Size``` annotation is used for setting a minimum and maximum size requirement. 
Email field- ```@Email``` is a Hibernate-Validator annotation for validating email field. 
Empty String as null -
We want to convert all the empty strings coming from browser as null. Spring supports (http://docs.spring.io/spring/docs/current/spring-framework-reference/htmlsingle/#mvc-ann-webdatabinder) binder for conversion. See method initBinder in BaseController. 

Datatype Error Handling
Annotation @Valid along with BindingResult will handle the datatype errors. 
Ex: (In StudentController.java)
```@ResponseBody```
   @RequestMapping(value = "Save")
    public JTableResult Save(HttpServletRequest request, @Valid Student student, BindingResult bindingResult)
```
BindingResult is Spring’s object that holds the result of the validation. If any errors have occurred during validation, they are stored in BindingResult. 

######Handling Date Format 
1. Date from controller to jTable
jTable expects the given date string to be formatted as one of the samples shown below:
- Date(1320259705710)
- 2011-01-01 20:32:42 (YYYY-MM-DD HH:MM:SS)
- 2011-01-01 (YYYY-MM-DD)
I have used the first format. Class JsonDateSerializer converts the date string into the selected format (first one).  The annotation @JsonSerialize(using = JsonDateSerializer.class) for date field  in Student.java will help achieve that. 

2. Date from jTable to controller
jTable uses jquery UI datepicker date format. For valid date formats see section $.datepicker.formatDate( format, date, options ) at http://api.jqueryui.com/datepicker/ .
I have used dd/mm/yy date format in my example. The defaultDateFormat for jTable is set in JavaScriptAndCSS.jsp. Corresponding date format for Java is dd/MM/yyyy. Annotation @DateTimeFormat(pattern = BaseController.DATE_FORMAT) sets the date format to dd/MM/yyyy.
The BaseController.DATE_FORMAT is accessing the DATE_FORMAT variable defined in BaseController.

######Themes 
jTable supports built-in themes as well as jQuery UI themes. I have used jQuery UI themes only. For this purpose jqueryuiTheme is set to true in JavaScriptAndCSS.jsp.

Getting jQuery UI theme (Example: blitzer theme)
Download the desired theme from http://jqueryui.com/themeroller/ . Open the zip folder and go to the images folder. Copy all images from the images folder to web/css/images folder of your project. Copy jquery-ui.theme.css file as jquery-ui.blitzer.css in web/css folder. Replace blitzer by name of the theme you downloaded. Then change reference in JavaScriptAndCSS.jsp as follows.

```<link rel="stylesheet" type="text/css" href="../css/jquery-ui.blitzer.css" />```


######Database Access Setup
Project database access setup is done in ProjectJDBC.xml. HSQLDB was selected for the demo due to its ease-of-use. The value attribute of property with name=”url” is set to the path where database will be created if it does not exist. 

```
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
    <property name="driverClassName" value="org.hsqldb.jdbcDriver"/>
    <property name="url" value="jdbc:hsqldb:file:C:\DB\StudentDB;shutdown=true;hsqldb.default_table_type=cached;sql.enforce_strict_size=false"/>
    <property name="username" value="sa"/>
    <property name="password" value=""/>
</bean>
```
ProjectDAO.java and ProjectJDBC.java are used for setting up database access in SpringJava. Method getJdbcTemplate() in BaseController is used for returning the jdbcTemplate. For details refer:
http://docs.spring.io/spring/docs/current/spring-framework-reference/html/jdbc.html .

The DataBase Setup button in the main menu calls the DatabaseSetupController. DataBaseSetupController was defined to create the tables and insert records. Every time the button is clicked, database will be populated with the seed records to get the user started. 

######KeyHolder
Create table statement defines self generating id as primary key for each table. Every time a record is inserted, the id value increments by 1. In order to obtain the generated id value KeyHolder is used in insert() function in each of the model classes. 
```
KeyHolder holder = new GeneratedKeyHolder();
jdbcTemplate.update(psc, holder);
id = holder.getKey().intValue();

JdbcTemplate jdbcTemplate = getJdbcTemplate();
if (action == 1) student.insert(jdbcTemplate);
else student.update(jdbcTemplate);
student = Student.retrieveById(jdbcTemplate, student.id);
rslt.Result = "OK";
rslt.Record = student;
return rslt;
```
The above code snippet is part of Save() function of StudentController.java. Student.retrieveById is used to get the record and given to .Record parameter as per jTable requirement. Hence, KeyHolder gives the generated id which is passed as parameter in retrieveById() in case of insert action.

######Artifacts
I used IntelliJ IDEA 2016.1.1 for the demo. For deploying the code correctly, make sure you do the following steps correctly:
- Select the Project Structure icon ![Project Structure Icon](https://github.com/pujagani/JTableSpringDemo/blob/master/screenshots/project_structure.png)
- Go to Artifacts tab.
- Change the Output directory path to appropriate path.
[![Changing path in Artifact](https://github.com/pujagani/JTableSpringDemo/blob/master/screenshots/artifact.PNG)

######Using other databases
In order to use other databases, the database access setup part will change and create table statement in each of the model classes will change according to the syntax for the selected database.

###### Screenshots of Student Table
[![Menu and Student Table](https://github.com/pujagani/JTableSpringDemo/blob/master/screenshots/StudentTable.PNG "Menu and Student Table")
[![Edit Student Record Dialog](https://github.com/pujagani/JTableSpringDemo/blob/master/screenshots/editStudentTable.PNG "Edit Student Record Dialog")
[![Master Table- Student, Child Table- Exam Results](https://github.com/pujagani/JTableSpringDemo/blob/master/screenshots/MasterChildTable.PNG "Master Child Table- Student - Exam Results")




