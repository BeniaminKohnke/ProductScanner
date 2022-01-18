package com.scanner.productscanner;

import java.util.ArrayList;

public class Logger {
    private static ArrayList<String> loggerQueue = new ArrayList<>();

    public enum LogLevel {
        _INFO, _WARN, _ERROR, INFO, WARN, ERROR,
    }

    /**
     * Adds log to Logger's queue.
     * @param callerName - method and class where method is used.
     * @param message - log message.
     * @param level - used to distinguish log importance. Logs with "_" level will not be stored in database.
     */
    public static void log(String callerName, String message, LogLevel level){
        if(!level.toString().contains("_")){
            DataAccess.saveLog(callerName, message, level);
        }
        loggerQueue.add(level.name() + " -> " + callerName + " -> " + message);
    }

    /**
     * Returns and removes every message from Logger's queue.
     */
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
