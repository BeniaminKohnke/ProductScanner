package com.scanner.productscanner;

import java.io.*;
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
        if(value.equals("search")){
            new Thread(AlgorithmExecutor::searchForProducts).start();
            response.getWriter().write("SEARCHING");
        }else if(value.equals("scan")){
            new Thread(AlgorithmExecutor::scan).start();
            response.getWriter().write("SCANNING");
        }else if(value.equals("log")){
            response.getWriter().write(Log.getSingleMessage());
        }
    }
}