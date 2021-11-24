package com.scanner.productscanner;

import java.io.*;
import java.util.ArrayList;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/AjaxServlet")
public class AjaxServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String value = request.getParameter("value");
        if(value == null || value.isEmpty()){
            value = "";
        }
        response.setContentType("text/plain");
        switch (value) {
            case "search": {
                String message = AlgorithmExecutor.LOCK ? "-" : "SEARCHING";
                new Thread(AlgorithmExecutor::searchForProducts).start();
                response.getWriter().write(message);
                break;
            }
            case "scan": {
                String message = AlgorithmExecutor.LOCK ? "-" : "SCANNING";
                new Thread(AlgorithmExecutor::scan).start();
                response.getWriter().write(message);
                break;
            }
            case "log":{
                String message = "-";
                ArrayList<String> messages = Logger.getEveryMessage();
                if(messages != null){
                    StringBuilder builder = new StringBuilder();
                    for (String m : messages) {
                        builder.append(m);
                        builder.append("|");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    message = builder.toString();
                }
                response.getWriter().write(message);
                break;
            }
        }
    }
}