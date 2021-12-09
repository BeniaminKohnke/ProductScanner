package com.scanner.productscanner;

import java.util.ArrayList;

public class Logger {
    private static ArrayList<String> loggerQueue = new ArrayList<>();

    public enum LogLevel {
        _INFO, _WARN, _ERROR, INFO, WARN, ERROR,
    }

    public static void log(String callerName, String message, LogLevel level){
        //logs with "_" level will not be stored in database
        if(!level.toString().contains("_")){
            DataAccess.saveLog(callerName, message, level);
        }
        loggerQueue.add(level.name() + " -> " + callerName + " -> " + message);
    }

    public static ArrayList<String> getEveryMessage(){
        ArrayList<String> messages = null;
        if(!loggerQueue.isEmpty()){
            synchronized (loggerQueue){
                messages = new ArrayList<>(loggerQueue);
                loggerQueue.clear();
            }
        }
        return messages;
    }
}
