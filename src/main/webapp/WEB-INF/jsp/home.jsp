<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>home</title>
    <style>
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f2f2f2;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0;
        }

        .container {
            max-width: 800px;
            margin: 30px auto;
        }

        .card {
            border: none;
            border-radius: 10px;
            margin-bottom: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            transition: transform 0.3s ease-in-out;
        }

        .card:hover {
            transform: scale(1.05);
        }

        .card-body {
            text-align: center;
        }

        .card-title {
            font-size: 1.5rem;
            font-weight: bold;
            color: #007bff;
            margin-bottom: 10px;
        }

        .card-text {
            font-size: 1rem;
            color: #555;
            line-height: 1.4;
            margin-bottom: 8px;
        }

        .footer {
            background-color: #f8f8f8;
            padding: 10px;
            text-align: center;
        }

        #loading-container {
            display: none;
            justify-content: center;
            align-items: center;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(255, 255, 255, 0.8);
        }

        #loading {
            border: 8px solid #f3f3f3;
            border-top: 8px solid #3498db;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            animation: spin 3s linear infinite;
        }

        @keyframes spin {
            0% { transform: rotate(0deg); }
            100% { transform: rotate(360deg); }
        }
    </style>
</head>
<body>
    <div id="loading-container">
        <div id="loading"></div>
    </div>

    <div class="container">
        <h1 class="text-center mb-4">${welcome}</h1>

        <c:forEach var="chore" items="${chores}">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title"><a href="<c:url value='/choreDetails/${chore.choreId}'/>">${chore.choreName}</a></h5>
                    <p class="card-text">${roommates[chore.currIndex].name}</p>
                    <c:if test="${chore.day == null}">
                    <p class="card-text">${chore.choreType}</p>
                    </c:if>
                    <c:if test="${chore.day != null}">
                        <p class="card-text">${chore.day}</p>
                    </c:if>
                </div>
            </div>
        </c:forEach>

        <div class="footer">
            <!-- Your existing footer content -->
            <a href="<c:url value='/logout'/>" class="btn btn-danger">LOGOUT</a>
        </div>
    </div>

    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
</body>
</html>
