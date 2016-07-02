<%@ page import="model.*" %>
<html>
<head>
  <title>Course List</title>
  <%@ include file="JavascriptAndCSS.jsp" %>
</head>
<body>
<script>
$( document ).ready(function() {
var JTableInfo =
   {
        title: 'Course List',
        paging: true,
        pageSize: 10,
        sorting: true,
        defaultSorting: 'name ASC',
        actions: {
            listAction: 'Course/List',
            createAction: 'Course/Save?action=1',
            updateAction: 'Course/Save?action=2',
            deleteAction: 'Course/Delete'
        },
        fields: {
            id: {
                key: true,
                create: false,
                edit: false,
                list: false
            },
            name: {
                title: 'Name',
                width: '23%'
            },
        }
    }

   $('#JTable').jtable(JTableInfo);
   $('#JTable').jtable('load');

});
</script>
</body>
</html>