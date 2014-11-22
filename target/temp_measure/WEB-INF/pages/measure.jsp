<%--
  Created by IntelliJ IDEA.
  User: Beta
  Date: 11/21/14
  Time: 6:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>

<html>
<head>

    <script type="text/javascript">

    </script>

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Temperature, Humidity</title>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <style type="text/css">
        ${demo.css}
    </style>
    <script type="text/javascript">
        var id = <c:out value="${measureObj.idMeasurment}" />;
        $(function () {

            http://localhost:8080/getMeasure?id=4&compression=4
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


            function drawChart(){
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
        });

    </script>

</head>
<body>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>

<div id="container" style="min-width: 310px; height: 400px; margin: 0 auto"></div>
</body>

</html>
