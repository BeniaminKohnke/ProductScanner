<%@ page import="com.scanner.productscanner.AlgorithmExecutor" %>
<%@ page import="com.scanner.productscanner.Log" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Scanner</title>
        <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
    </head>
    <%!
        AlgorithmExecutor executor = new AlgorithmExecutor();
        Log logger = new Log();
        public void execute(String msg){
            if(msg.equals("prospector")){
                executor.algorithmMode = AlgorithmExecutor.AlgorithmMode.SEARCH_FOR_EXAMPLES;
                executor.execute();
            }
            if(msg.equals("scanner")){
                executor.algorithmMode = AlgorithmExecutor.AlgorithmMode.SCAN;
                executor.execute();
            }
        }
    %>
    <body>
        <h1><%= "Product scanner" %></h1>
        <input id="prospector" type="submit"/>
        <input id="scanner" type="submit"/>
    </body>

    <script type="text/javascript">
        $(document).ready(function() {
            $('#prospector').click(function (){
                $.ajax({
                    type: "post",
                    url: "/path",
                    data: "prospector",
                    success: function(msg){

                    }
                });
            });
            $('#scanner').click(function (){
                $.ajax({
                    type: "post",
                    url: "/path",
                    data: "prospector",
                    success: function(msg){

                    }
                });
            });
        });
    </script>
</html>