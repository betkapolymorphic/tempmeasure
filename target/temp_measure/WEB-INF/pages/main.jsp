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
    <script type="text/javascript" src="/resources/js/jquery.js"></script>
    <script type="text/javascript" src="/resources/js/jquery.tablesorter.min.js"></script>
    <script type="text/javascript" src="/resources/js/bootstrap.min.js"></script>
    <style type="text/css">
        ${demo.css}
        .bordered {
            border: 2px solid;
            border-radius: 25px;
        }
        .container {
            padding-top: 40px;
        }
    </style>

    <link rel="stylesheet" href="/resources/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet" href="/resources/css/bootstrap-theme.min.css">

    <link rel="stylesheet" type="text/css" href="/resources/css/jquery.datetimepicker.css"/>
    <link rel="stylesheet" type="text/css" href="/resources/css/style.css"/>
    <script>
        $(document).on('click', '.number-spinner button', function () {
            var btn = $(this),
                    oldValue = btn.closest('.number-spinner').find('input').val().trim(),
                    newVal = 0;

            if (btn.attr('data-dir') == 'up') {
                newVal = parseInt(oldValue) + 1;
            } else {
                if (oldValue > 1) {
                    newVal = parseInt(oldValue) - 1;
                } else {
                    newVal = 1;
                }
            }
            btn.closest('.number-spinner').find('input').val(newVal);
        });
        $(function () {
            $("#mytable").tablesorter();
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
        function showAllMeasure(){
            $.get("/api/getMeasureAll?periodHours=" + $("#measure_period").val(),function(data){
                if(data.trim()==""){
                    return;
                }
                result = data.trim();
                result = jQuery.parseJSON(result);
                var ranges = [];
                var averages = [];
                for(var i=0;i<result.samples.length;i++){
                    var cur = result.samples[i];

                    var date = Number(cur.mid.timespan);
                    ranges.push([date,cur.min.temperature == undefined ? null : cur.min.temperature
                        ,cur.max.temperature == undefined ? null : cur.max.temperature]);

                    averages.push([date,cur.mid.temperature=="null" ? null : cur.mid.temperature]);


                }
                drawChartAllMeasure(result.title,averages,ranges);
            });
        }
        function drawChartAllMeasure(RTitle,averages,ranges){

            $("#container").show();
            $('#container').highcharts({

                title: {
                    text: RTitle
                },

                xAxis: {
                    type: 'datetime'
                },

                yAxis: {
                    title: {
                        text: null
                    }
                },

                tooltip: {
                    crosshairs: true,
                    shared: true,
                    valueSuffix: '°C'
                },

                legend: {
                },

                series: [{
                    name: 'Temperature',
                    data: averages,
                    zIndex: 1,
                    marker: {
                        fillColor: 'white',
                        lineWidth: 2,
                        lineColor: Highcharts.getOptions().colors[0]
                    }
                }, {
                    name: 'Range',
                    data: ranges,
                    type: 'arearange',
                    lineWidth: 0,
                    linkedTo: ':previous',
                    color: Highcharts.getOptions().colors[0],
                    fillOpacity: 0.3,
                    zIndex: 0
                }]
            });
        }

    </script>
    <title></title>
</head>
<body >
<div align="center" id="container" style="min-width: 310px; height: 400px; margin: 0 auto;display: none;" ></div>


<table class="tablesorter" border="0" cellpadding="0" cellspacing="1" align="center" id="mytable" >
    <thead>
    <tr>

            <th>Id measurm</th>
            <th>Date</th>
            <th>Location</th>
            <th>Count samples</th>
            <th>Operation</th>

    </tr>
    </thead>
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
<div class="container bordered" align = "center">
    <div class="row">
        <div class="col-xs-3 col-xs-offset-3" style="margin-left:38%">
            <p>Period (hours)</p><br>
            <div class="input-group number-spinner">

				<span class="input-group-btn">
					<button class="btn btn-default" data-dir="dwn"><span class="glyphicon glyphicon-minus"></span></button>
				</span>
                <input type="text" id="measure_period" class="form-control text-center" value="24" placeholder="measure period hours">
				<span class="input-group-btn">
					<button class="btn btn-default" data-dir="up"><span class="glyphicon glyphicon-plus"></span></button>
				</span>
            </div>
            <button onclick="showAllMeasure()">Show all Temperature</button><br>
        </div>
    </div>
</div>


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
<!--<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>-->
<script src="/resources/js/highcharts.js"></script>
<script src="/resources/js/highcharts-more.js"></script>
<script src="/resources/js/modules/exporting.js"></script>

</body>
<!--<script src="http://colorchicken.esy.es/temp_measur/jquery.datetimepicker.js"></script>
-->
<script src="/resources/js/jquery.datetimepicker.js"></script>

</html>
