<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Questionary</title>
    <style>
        body, button{
            font-size: large;
        }
        div {
            margin-bottom: 10px;
        }
        table,tr,td {
            border: 1px solid black;
        }
        td {
            padding: 10px;
        }
    </style>
</head>
<body>
<h1><%= "Questionary" %>
</h1>
<br/>
<% String getPage = (String)session.getAttribute("page"); %>
<% if (getPage == null || "form".equals(getPage)) { %>
    <form action="/Homework3_war_exploded/form" method="POST">
        1) Do you love Java? <br/>
        <input type="radio" id="javayes" name="java" value="yes" >
        <label for="javayes">Yes</label>
        <input type="radio" id="javano" name="java" value="no" >
        <label for="javano">No</label>
        <br/><br/>
        2) Do you love Python? <br/>
        <input type="radio" id="pythonyes" name="python" value="yes" >
        <label for="pythonyes">Yes</label>
        <input type="radio" id="pythonno" name="python" value="no" >
        <label for="pythonno">No</label>
        <br/><br/>
        <button type="submit">Submit</button>
    </form>
<% } else if ("answer".equals(getPage)) {
    String java = (String)session.getAttribute("java");
    String python = (String)session.getAttribute("python");%>
    <div>Below you can see what you like: </div>
    <div>
        <table>
            <tr>
                <td>
                    Java
                </td>
                <td>
                    <%= java %>
                </td>
            </tr>
            <tr>
                <td>
                    Python
                </td>
                <td>
                    <%= python %>
                </td>
            </tr>
        </table>
    </div>
    <div><a href="/Homework3_war_exploded/form?page=form">Return to Questionary form</a></div>
<% } %>
</body>
</html>