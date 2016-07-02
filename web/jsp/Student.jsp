<%@ page import="model.*" %>
<html>
<head>
  <title>Student List</title>
  <%@ include file="JavascriptAndCSS.jsp" %>
</head>
<body>
<script>
var error = '${Error}';
var cities = ${OptionsCity};
var courses = ${OptionsCourse};

$(document).ready(function () {
    if (error) {
        alert(error);
        return;
    }

    function studentPhone(studentData) {
        var $img = $('<img src="../css/img/phone.png" title="Edit phone numbers" />');
        $img.css({cursor: 'pointer'});
        $img.click(function () {
            $('#JTable').jtable('openChildTable',
                    $img.closest('tr'),
                    {
                        title: studentData.record.name + ' - Phone numbers',
                        actions: {
                            listAction: 'StudentPhone/List?student_id=' + studentData.record.id,
                            createAction: 'StudentPhone/Save?action=1',
                            updateAction: 'StudentPhone/Save?action=2',
                            deleteAction: 'StudentPhone/Delete'
                        },
                        fields: {
                            student_id: {
                                type: 'hidden',
                                defaultValue: studentData.record.id
                            },
                            id: {
                                key: true,
                                create: false,
                                edit: false,
                                list: false
                            },
                            phone_type: {
                                title: 'Phone type',
                                width: '30%',
                                options: { '1': 'Home phone', '2': 'Office phone', '3': 'Cell phone' }
                            },
                            phone_number: {
                                title: 'Phone Number',
                                width: '30%'
                            },
                            record_date: {
                                title: 'Record date',
                                width: '20%',
                                type: 'date',
                                create: false,
                                edit: false
                            }
                        }
                    }, function (data) {
                        data.childTable.jtable('load');
                    });
        });
        return $img;
    }

    function studentResult(studentData) {
        var $img = $('<img src="../css/img/exam.png" title="Edit exam results" />');
        $img.css({cursor: 'pointer'});
        $img.click(function () {
            $('#JTable').jtable('openChildTable',
                    $img.closest('tr'),
                    {
                        title: studentData.record.name + ' - Exam Results',
                        actions: {
                            listAction: 'StudentResult/List?student_id=' + studentData.record.id,
                            createAction: 'StudentResult/Save?action=1',
                            updateAction: 'StudentResult/Save?action=2',
                            deleteAction: 'StudentResult/Delete'
                        },
                        fields: {
                            student_id: {
                                type: 'hidden',
                                defaultValue: studentData.record.id
                            },
                            id: {
                                key: true,
                                create: false,
                                edit: false,
                                list: false
                            },
                            course_name: {
                                title: 'Course name',
                                width: '40%',
                                options: courses
                            },
                            exam_date: {
                                title: 'Exam date',
                                width: '30%',
                                type: 'date'

                            },
                            degree: {
                                title: 'Degree',
                                width: '10%',
                                options: ["AA", "BA", "BB", "CB", "CC", "DC", "DD", "FF"]
                            }
                        }
                    }, function (data) { //opened handler
                        data.childTable.jtable('load');
                    });
        });
        return $img;
    }

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
               exams: {
                   title: '',
                   width: '2%',
                   sorting: false,
                   edit: false,
                   create: false,
                   display: studentResult
               },
               name: {
                   title: 'Name',
                   width: '23%'
               },
               email: {
                   title: 'Email address',
                   list: false
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
               },
               education: {
                   title: 'Education',
                   list: false,
                   type: 'radiobutton',
                   options: { '1': 'Primary school', '2': 'High school', '3': 'University' }
               },
               about: {
                   title: 'About this person',
                   type: 'textarea',
                   list: false
               },
               active_flg: {
                   title: 'Status',
                   width: '12%',
                   type: 'checkbox',
                   values: { 'N': 'Inactive', 'Y': 'Active' },
                   defaultValue: 'Y'
               },
               record_date: {
                   title: 'Record date',
                   width: '15%',
                   type: 'date',
                   create: false,
                   edit: false,
                   sorting: false
               }
           }
       }

    $('#JTable').jtable(JTableInfo);
    $('#JTable').jtable('load');

});
</script>
</body>
</html>