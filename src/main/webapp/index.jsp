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
            <span id="lastAction">LAST ACTION : NONE</span>
            <table id="logs">
            </table>
            <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
            <script>
                document.getElementById("search").addEventListener("click", function() {
                    $.get('AjaxServlet', {
                        value : "search"
                    }, function(responseText) {
                        if(responseText !== "-"){
                            $(`#lastAction`).text("LAST ACTION : " + responseText);
                        }
                    });
                });
            </script>
            <script>
                document.getElementById("scan").addEventListener("click", function() {
                    $.get('AjaxServlet', {
                        value : "scan"
                    }, function(responseText) {
                            if(responseText !== "-"){
                                $(`#lastAction`).text("LAST ACTION : " + responseText);
                            }
                        });
                    });
            </script>
            <script>
                setInterval(() => {
                    $.get('AjaxServlet', {
                        value : "log"
                    }, function(responseText) {
                        if(responseText !== "-"){
                            let lines = responseText.split("|");
                            for(let i = 0; i < lines.length; i++){
                                let row = document.getElementById("logs").insertRow(0);
                                let cell = row.insertCell(0);
                                cell.innerHTML = lines[i];
                            }
                        }
                    });
                }, 100);
            </script>
        </form>
    </body>
</html>