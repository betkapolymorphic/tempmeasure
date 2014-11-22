<%--
  Created by IntelliJ IDEA.
  User: Beta
  Date: 11/21/14
  Time: 10:04 AM
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <meta charset="utf-8">
    <title>Raphaël · Analytics</title>
    <link rel="stylesheet" href="http://colorchicken.esy.es/temp_measur/demo.css" type="text/css" media="screen">
    <link rel="stylesheet" href="http://colorchicken.esy.es/temp_measur/demo-print.css" type="text/css" media="print">
    <script src="http://colorchicken.esy.es/temp_measur/raphael.js"></script>
    <script src="http://colorchicken.esy.es/temp_measur/popup.js"></script>
    <script src="http://colorchicken.esy.es/temp_measur/jquery.js"></script>
    <script src="http://colorchicken.esy.es/temp_measur/analytics.js"></script>


</head>
<body>
    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <h2>Welcome : ${name}
            | <a href="/j_spring_security_logout" >Logout</a>" </h2>
    </c:if>
    <c:forEach var="measurm" items="${measurments}">
        <h3>Id measurment : <c:out value="${measurm.idMeasument}"/></h3>
        <table id="data">
            <tfoot>
                <tr>
                   <!-- <th>Humidity</th>-->
                   <!-- <th>Temperatur</th>-->
                <c:set var="count" scope="page" value="0"/>
                <c:forEach var="sample_record" items="${measurm.samples}">
                    <th><c:out value="${count}"/></th>
                    <c:set var="count" value="${count + 1}" scope="page"/>
                </c:forEach>
            </tr>
            </tfoot>
            <tbody>
                <c:forEach var="sample_record" items="${measurm.samples}">

                        <tr>
                                <!--<td><c:out value="${sample_record.humidity}"/></td>
                                -->
                                <td><c:out value="${sample_record.temperature}"/></td>
                        </tr>

                </c:forEach>
            </tbody>

        </table>
    </c:forEach>
    <br>
    <div id="holder"></div>


    <h1>Upload file!</h1>
    <form method="POST" action="uploadFile" enctype="multipart/form-data">
        File to upload: <input type="file" name="file"><br />
        Name: <input type="text" name="name"><br /> <br />
        <input type="submit" value="Upload"> Press here to upload the file!
    </form>



</body>
</html>
