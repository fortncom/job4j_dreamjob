<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.store.PsqlStore" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <title>Работа мечты</title>
</head>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<script>
    function validate() {
        const answer = "Please ";
        let result = answer;
        if ($('#fieldName').val() === '') {
            result += "-fill field Name ";
        }
        if ($('#citySelection').val() == null) {
            result += "-Choose City";
        }
        if (answer !== result) {
            alert(result);
            return false;
        }
        return true;
    }
</script>
<script>
    function addCity() {
        $.ajax({
            type: 'POST',
            crossDomain: true,
            url: 'http://localhost:8080/dreamjob/city.do',
            data: JSON.stringify({name: $('#city1').val()}),
            dataType: 'json'
        }).done(function (data) {
            $('#citySelection option:last').after('<option>' + data.name + '</option>')
        }).fail(function (err) {
            console.log(err);
        });
    }

    $(document).ready(function () {
        $.ajax({
            type: 'GET',
            crossDomain: true,
            url: '/dreamjob/city.do',
            dataType: 'json'
        }).done(function (data) {
            for (var city in data) {
                $('#citySelection option:last').after('<option>' + data[city].name + '</option>')
            }
        }).fail(function (err) {
            console.log(err);
        });
    });
</script>
<body>
<div class="container">
    <ul class="nav">
        <c:if test="${user != null}">
            <li class="nav-item">
                <a class="nav-link" href="<%=request.getContextPath()%>/logout.do"> <c:out value="${user.name}"/> | Выйти</a>
            </li>
        </c:if>
    </ul>
</div>
<%
    String id = request.getParameter("id");
    Candidate can = new Candidate(0, "", null, null);
    if (id != null) {
        can = PsqlStore.instOf().findCanById(Integer.parseInt(id));
    }
%>
<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) { %>
                Новый кандидат.
                <% } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=can.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input type="text" class="form-control" name="name" value="<%=can.getName()%>" id="fieldName">
                    </div>
                    <div class="container-fluid" style="margin: 20px auto;">
                        <label for="citySelection">Город</label>
                        <select class="form-control" id="citySelection" name="city">
                            <option selected disabled value="">Выберите город</option>
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate();">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
    <br>
    <form>
        <div class="form-group">
            <label for="city1" style="font-weight: bold">Другой город</label>
            <input type="text" class="form-control" id="city1" placeholder="Enter city">
        </div>
        <button type="button" class="btn btn-primary" onclick="addCity()">Save</button>
    </form>
</div>
</body>
</html>