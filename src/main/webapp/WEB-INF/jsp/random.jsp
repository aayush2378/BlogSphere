<%@page import="com.northeastern.choreless.controller.DumpController" %>
    <%@page import="com.northeastern.choreless.model.Roommate" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <%@ page import="java.util.List" %>
                <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
                        <!DOCTYPE html>

                        <html>

                        <head>
                            <title>Dumpster</title>
                            <style>
                                body {
                                    font-family: sans-serif;
                                    margin: 0;
                                    padding: 0;
                                    background-color: #f2f2f2;
                                    display: flex;
                                    justify-content: center;
                                    align-items: center;
                                    height: 100vh;
                                }

                                #skip {
                                    color: orange;
                                }

                                container {
                                    max-width: 80%;
                                    background-color: #fff;
                                    padding: 30px;
                                    margin: 20px auto;
                                    box-shadow: 0px 2px 10px rgba(0, 0, 0, 0.1);
                                    border-radius: 12px;
                                    border: 2px solid #007bff;
                                }


                                h1,
                                h2,
                                h3 {
                                    font-family: 'Roboto', sans-serif;
                                    font-weight: bold;
                                    color: #007bff;
                                }

                                h5 {
                                    color: #f44336;
                                    font-weight: bold;
                                }

                                p {
                                    font-size: 16px;
                                    line-height: 1.5;
                                    color: #333;
                                }

                                /* Form styles */
                                form {
                                    display: flex;
                                    flex-direction: column;
                                    align-items: center;
                                    margin-top: 20px;
                                }

                                label {
                                    display: block;
                                    margin-bottom: 10px;
                                    font-weight: bold;
                                }

                                input,
                                select {
                                    width: 100%;
                                    padding: 10px;
                                    border: 1px solid #ccc;
                                    border-radius: 5px;
                                }

                                #skipbutton {
                                    width: 80%;
                                    padding: 10px;
                                    border: 1px solid #ccc;
                                    border-radius: 5px;
                                    margin-right: auto;
                                }

                                #skipCount {
                                    width: 20%;
                                    /* Adjust this value as needed */
                                    padding: 10px;
                                    border: 1px solid #ccc;
                                    border-radius: 5px;
                                    box-sizing: border-box;
                                    min-width: 60px;
                                }

                                #flexing {
                                    display: flex;
                                    flex-direction: row;
                                    padding: 10px;
                                    border: 1px solid #ccc;
                                    border-radius: 5px;
                                    box-sizing: border-box;
                                    width: 200px;
                                }

                                /* Add this style for sliding animation */
                                h3 {
                                    opacity: 0;
                                    transform: translateY(20px);
                                    transition: opacity 0.5s ease, transform 0.5s ease;
                                }

                                h3.show {
                                    opacity: 1;
                                    transform: translateY(0);
                                }

                            </style>
                            <script src="https://code.jquery.com/jquery-3.6.4.min.js">

                            </script>
                            <script>
                                document.addEventListener("DOMContentLoaded", function () {
                                    var nextChoreDoer = document.getElementById("nextChoreDoer");

                                    // Function to add the 'show' class and trigger the slide-in animation
                                    function showNextChoreDoer() {
                                        nextChoreDoer.classList.add("show");
                                    }
                                    var isErrorEmpty = ${empty requestScope.error};
                                    // Check if requestScope.error is empty
                                    if (isErrorEmpty) {
                                    // Show the element initially
                                    showNextChoreDoer();
                                    
                                    }
                                    else {
                                        nextChoreDoer.style.transition = "none";
                                        showNextChoreDoer();
                                    }
                                });
                            </script>
                        </head>

                        <body>
                            <header>
                                <a href="<c:url value='/home?groupId=${applicationScope.groupId}'/>">HOME</a>
                                <a href="<c:url value='/logout' />">LOGOUT</a>
                            </header>

                            <container>
                                <form id="submitForm" method="post">
                                    <c:set var="index" value="${applicationScope.index}" />
                                    <c:set var="roommates" value="${applicationScope.roommates}" />
                                    <c:set var="debtorsInLine" value="${applicationScope.debtors_in_line}" />
                                    <c:set var="roommate_ids" value="${applicationScope.roommate_ids}" />
                                    <c:set var="sacrifice_arr" value="${applicationScope.sacrifice}" />
                                    <h2>${chore_name}</h2>
                                    <h3 id="nextChoreDoer"> Next Chore Doer: ${not empty debtorsInLine ?
                                        roommates[debtorsInLine[0]] :
                                        roommates[index]}</h3>
                                    <h5>${requestScope.error}</h5>

                                    <div id="dropdownContainer"></div>
                                    <p id="skip">${applicationScope.skip}</p>
                                    <input type="submit" value="SUBMIT" /> &nbsp; <input type="button"
                                        id="sacrificeButton" value="SACRIFICE" />

                                </form>
                                <form id="skipForm" method="post">
                                    <div id="flexing">
                                        <input type="submit" id="skipbutton" value="SKIP" />
                                        <select id="skipCount" name="skipCount" name="skipValue">
                                            <c:forEach var="i" begin="1" end="${sacrifice_arr[index]}">
                                                <option value="${i}">${i}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </form>
                                <p>Last Submission Time: <%= application.getAttribute("lastSubmissionTime")%>
                                </p>

                                <input type="hidden" name="group_hidden" value="${groupId}">
                                <h4>Skipping Points:</h4>
                                <table border="1">
                                    <thead>
                                        <tr>
                                            <th>Roommates</th>
                                            <th>Points</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="ind" begin="0" end="${fn:length(roommates)-1}" step="1">
                                            <tr>
                                                <td>${roommates[ind]}</td>
                                                <td>${sacrifice[ind]}</td>
                                                <td><a href="javascript:void(0);"
                                                        onclick="displayAlert(${ind})"><i>debtors</i></a>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>

                            </container>


                            <script>

                                <c:set var="groupId" value="${applicationScope.groupId}" />
                                var id = ${ groupId };

                                // Get the form element
                                var form = document.getElementById('submitForm');

                                // Set the action attribute with the variable value
                                // form.action = 'http://localhost:8080/dump/' + id;
                                var baseUrl = window.location.protocol + '//' + window.location.host;
                                form.action = baseUrl + '/dump/' + id;

                                // href="<c:url value='/logout' />"

                                var skipform = document.getElementById('skipForm');

                                // Set the action attribute with the variable value
                                // skipform.action = 'http://localhost:8080/skip/' + id;
                                baseUrl = window.location.protocol + '//' + window.location.host;
                                skipForm.action = baseUrl + '/skip/' + id;


                                var debtorsInLine = [];

                                <c:forEach items="${debtorsInLine}" var="debtors">
                                    debtorsInLine.push("${debtors}");
                                </c:forEach>


                                var index = ${ index };

                                // var sacrifice_arr =[];

                                // <c:forEach items="${sacrifice_arr}" var="s">
                                //     sacrifice_arr.push("${sacrifice}");
                                // </c:forEach>



                                if (debtorsInLine.length > 0) {
                                    index = parseInt(debtorsInLine[0], 10);
                                }

                                // console.log(debtorsInLine, index);

                                var roommates = [];

                                <c:forEach items="${roommates}" var="item">
                                    roommates.push("${item}");
                                </c:forEach>

                                var roommate_ids = [];

                                <c:forEach items="${roommate_ids}" var="item">
                                    roommate_ids.push("${item}");
                                </c:forEach>

                                var condition = (parseInt("${sacrifice_arr[index]}") > 0)                 /////////////////////////////////////////////

                                if (!condition) {
                                    document.getElementById("skipCount").disabled = true;
                                }


                                document.addEventListener("DOMContentLoaded", function () {
                                    var sacrificeButton = document.getElementById("sacrificeButton");
                                    var dropdownContainer = document.getElementById("dropdownContainer");

                                    sacrificeButton.addEventListener("click", function () {
                                        var newDropdown = document.createElement("select");

                                        newDropdown.name = "sacrificer";

                                        for (var i = 0; i < roommates.length; i++) {
                                            if (i !== index) {
                                                var option = document.createElement("option");
                                                option.value = roommate_ids[i];
                                                option.text = roommates[i];
                                                newDropdown.appendChild(option);
                                            }
                                        }

                                        dropdownContainer.innerHTML = 'Choose the Sacrificer: ';
                                        dropdownContainer.appendChild(newDropdown);
                                    });
                                });

                                function displayAlert(index) {
                                    var groupId = document.querySelector("input[name='group_hidden']").value;
                                    $.ajax({
                                        type: 'GET',
                                        url: '/getDebtors/' + groupId,  // Replace with the actual URL to fetch debtors data
                                        data: { index: index },
                                        success: function (debtors) {
                                            // Construct the alert message
                                            var alertMessage = "Debtors who owe you (in chronological order):\n";
                                            for (var i = 0; i < debtors.length; i++) {
                                                alertMessage += roommates[debtors[i]] + "\n";
                                            }

                                            // Display the alert
                                            alert(alertMessage);
                                        },
                                        error: function () {
                                            alert('Error fetching debtors');
                                        }
                                    });
                                }



                            </script>

                            
                        </body>

                        </html>