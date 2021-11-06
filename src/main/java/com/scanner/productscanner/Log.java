package com.scanner.productscanner;

import java.util.ArrayList;

public class Log {
    private static ArrayList<String> loggerQueue = new ArrayList<>();

    public enum LogLevel{
        INFO, WARN, ERROR,
    }

    public static void log(String callerName, String message, LogLevel level){
        loggerQueue.add(level.name() + " -> " + callerName + " -> " + message);
    }

    public static String getSingleMessage(){
        String message = null;
        if(!loggerQueue.isEmpty()){
            message = loggerQueue.get(0);
            loggerQueue.remove(0);
        }
        return message;
    }
}
