package com.scanner.productscanner;

import java.util.ArrayList;

public class Log extends Thread {
    private static ArrayList<String> loggerQueue = new ArrayList<>();

    public enum LogLevel{
        INFO, WARN, ERROR,
    }

    public static void log(String callerName, String message, LogLevel level){
        loggerQueue.add(level.name() + " -> " + callerName + " -> " + message);
    }

    @Override
    public void run() {
        while (true){
            String message = "";
            synchronized(loggerQueue){
                if(!loggerQueue.isEmpty()){
                    message = loggerQueue.get(0);
                    loggerQueue.remove(0);
                }
            }
            if(!message.isEmpty()){
                System.out.println(message);
            }
        }
    }
}
