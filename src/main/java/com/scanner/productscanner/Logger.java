package com.scanner.productscanner;

import java.util.ArrayList;

public class Logger {
    private static ArrayList<String> loggerQueue = new ArrayList<>();

    public enum LogLevel{
        NONE, INFO, WARN, ERROR,
    }

    public static void log(String callerName, String message, LogLevel level){
        loggerQueue.add(level.name() + " -> " + callerName + " -> " + message);
    }

    public static String getSingleMessage(){
        String message = null;
        if(!loggerQueue.isEmpty()){
            synchronized (loggerQueue){
                message = loggerQueue.get(0);
                loggerQueue.remove(0);
            }
        }
        return message;
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
