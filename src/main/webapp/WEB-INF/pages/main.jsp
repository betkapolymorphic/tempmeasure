<%--
  Created by IntelliJ IDEA.
  User: Beta
  Date: 11/21/14
  Time: 6:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Temperature, Humidity</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <style type="text/css">
        ${demo.css}
        .bordered {
            border: 2px solid;
            border-radius: 25px;
        }
    </style>


    <link rel="stylesheet" type="text/css" href="http://colorchicken.esy.es/temp_measur/jquery.datetimepicker.css"/>
    <script>
        $(function () {
           $("#datetime").datetimepicker();
            $("#datebetween_begin").datetimepicker();
            $("#datebetween_end").datetimepicker();
            //$("#container").hide();
        });
        function removeMeasure(id){
            if (confirm('Are you sure you want delete this Measurment?')) {
                $.post("/removeMeasure?id="+id,function(data){
                   if(data=="success"){
                        $("#tr_"+id).remove();
                   }else{
                       alert("can't remove :(")
                   }

                });
            } else {
                // Do nothing!
            }
        }
        function showMeasure(id){
            $.ajax({ url: "/getMeasure?id="+id+"&compression=12",
                timeout: 80000 }).done(
                    function (data, status) {
                        if(data.trim()==""){
                            return;
                        }
                        temp=[];
                        hum=[];
                        titles = [];
                        result = data.trim();
                        result = jQuery.parseJSON(result);
                        for(var i=0;i<result.samples.length;i++){
                            temp.push(result.samples[i].temperature);
                            hum.push(result.samples[i].humidity);
                            titles.push(result.samples[i].date);
                        }
                        drawChart();
                    });
        }
        function showMeasureBetween(){
            $.ajax({ url: "/api/getMeasurePeriod?date1="+$("#datebetween_begin").val()
                    +"&date2="+$("#datebetween_end").val(),
                timeout: 80000}).done(
                    function (data, status) {
                        if(data.trim()==""){
                            return;
                        }
                        temp=[];
                        hum=[];
                        titles = [];
                        result = data.trim();
                        result = jQuery.parseJSON(result);
                        for(var i=0;i<result.samples.length;i++){
                            temp.push(result.samples[i].temperature);
                            hum.push(result.samples[i].humidity);
                            titles.push(result.samples[i].date);
                        }
                        drawChart();
                    });
        }
        function drawChart(){
            $("#container").show();
            $('#container').highcharts({
                chart: {
                    zoomType: 'xy'
                },
                title: {
                    text: 'Humidity, Temperature for Measurment id : '+result.idMesurment +' , date : '+result.Date
                },
                subtitle: {
                    text: ''
                },
                xAxis: [{
                    categories: titles
                }],
                yAxis: [{ // Primary yAxis
                    labels: {
                        format: '{value}°C',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    },
                    title: {
                        text: 'Temperature',
                        style: {
                            color: Highcharts.getOptions().colors[1]
                        }
                    }
                }, { // Secondary yAxis
                    title: {
                        text: 'Humidity',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    labels: {
                        format: '{value} hum',
                        style: {
                            color: Highcharts.getOptions().colors[0]
                        }
                    },
                    opposite: true
                }],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'left',
                    x: 120,
                    verticalAlign: 'top',
                    y: 100,
                    floating: true,
                    backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
                },
                series: [{
                    name: 'Humidity',
                    type: 'column',
                    yAxis: 1,
                    data: hum,
                    tooltip: {
                        valueSuffix: ' hum'
                    }

                }, {
                    name: 'Temperature',
                    type: 'spline',
                    data: temp,
                    tooltip: {
                        valueSuffix: '°C'
                    }
                }]
            });
        }
    </script>
    <title></title>
</head>
<body >
<div align="center" id="container" style="min-width: 310px; height: 400px; margin: 0 auto;display: none;" ></div>


<table border="5" align="center" >
    <tr>

            <th>Id measurm</th>
            <th>Date</th>
            <th>Location</th>
            <th>Count samples</th>
            <th>Operation</th>

    </tr>
    <c:forEach var="measurm" items="${measurments}">


        <tr id="tr_<c:out value="${measurm.idMeasurment}"/>">
            <td><c:out value="${measurm.idMeasurment}"/></td>
            <td><c:out value="${measurm.date}"/></td>
            <td><c:out value="${measurm.position}"/></td>
            <td><c:out value="${measurm.sizeSamples}"/></td>
            <td>
                 <button onclick="showMeasure(<c:out value="${measurm.idMeasurment}"/>)">show</button><br>
                 <button onclick="removeMeasure(<c:out value="${measurm.idMeasurment}"/>)"> Remove</button>
            </td>
        </tr>


    </c:forEach>
</table>
<div align="center" class="bordered">
    <h1>Search beetween</h1>
    Begin : <input type="text" name="date" id="datebetween_begin"><br /> <br />
    End : <input type="text" name="date" id="datebetween_end"><br /> <br />
    <button onclick="showMeasureBetween()">Show</button>
</div>
<div align="center" class="bordered">
    <h1>Add new</h1>
<form method="POST" action="uploadFile" enctype="multipart/form-data">
    Date: <input type="text" name="date" id="datetime"><br /> <br />
    File to upload: <input type="file" name="file"><br />
    Position: <input type="text" name="position"><br /> <br />

    Sample record frequency(sec)<input type="text" value="2.5"  name="frequency"><br /> <br />
    <input type="submit" value="Upload"> Upload!
</form>
</div>
<br>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>


</body>
<script src="http://colorchicken.esy.es/temp_measur/jquery.datetimepicker.js"></script>

</html>
