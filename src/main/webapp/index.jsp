<%@ page contentType="text/html; charset=UTF-16" pageEncoding="UTF-16" %>

<!DOCTYPE html>
<html lang="pl">
<head>
    <title>Scanner</title>
</head>
    <body>
        <form>
            <input type="button"  id="search" value="search">
            <input type="button" id="scan" value="scan">
            <input type="button" id="log" value="update logs">
            <span id="lastAction">LAST ACTION : NONE</span>
            <table id="logs">
            </table>
            <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
            <script type="text/javascript">
                document.getElementById("search").addEventListener("click", function() {
                    $(document).ready(function() {
                        $('#search').blur(function(event) {
                            $.get('AjaxServlet', {
                                value : "search"
                            }, function(responseText) {
                                $(`#lastAction`).text("LAST ACTION : " + responseText);
                            });
                        });
                    });
                });

                document.getElementById("scan").addEventListener("click", function() {
                    $(document).ready(function() {
                        $('#scan').blur(function(event) {
                            $.get('AjaxServlet', {
                                value : "scan"
                            }, function(responseText) {
                                $(`#lastAction`).text("LAST ACTION : " + responseText);
                            });
                        });
                    });
                });

                document.getElementById("log").addEventListener("click", function() {
                    $(document).ready(function() {
                        $('#log').blur(function(event) {
                            $.get('AjaxServlet', {
                                value : "log"
                            }, function(responseText) {
                                let row = document.getElementById("logs").insertRow(0);
                                let cell = row.insertCell(0);
                                cell.innerHTML = responseText;
                            });
                        });
                    });
                });
            </script>
        </form>
    </body>
</html>